package me.kofesst.spring.souvenirstore.controller.manager

import me.kofesst.spring.souvenirstore.database.PromoCodeDto
import me.kofesst.spring.souvenirstore.model.PromoCode
import me.kofesst.spring.souvenirstore.model.form.PromoCodeForm
import me.kofesst.spring.souvenirstore.repository.PromoCodesRepository
import me.kofesst.spring.souvenirstore.util.asModels
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/manager/promocodes")
@PreAuthorize("hasAnyAuthority('Manager')")
class PromoCodesController @Autowired constructor(
    private val repository: PromoCodesRepository,
) {
    @GetMapping
    fun overview(
        @RequestParam("query") query: String?,
        model: Model,
    ): String {
        val promoCodes = repository.findAll().asModels().run {
            if (query?.isNotBlank() == true) {
                model.addAttribute("query", query)
                filter { promoCode ->
                    promoCode.code.lowercase().contains(query.lowercase())
                }
            } else {
                this
            }
        }
        model.addAttribute("models", promoCodes)
        return "pages/manager/promocodes/overview"
    }

    @GetMapping("/add")
    fun add(
        promoCode: PromoCodeForm,
        model: Model,
    ): String {
        model.addAttribute("promoCode", promoCode)
        return "pages/manager/promocodes/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("promoCode") promoCodeForm: PromoCodeForm,
        result: BindingResult,
    ): String {
        val promoCode = checkPromoCode(
            promoCodeForm = promoCodeForm,
            result = result
        ) ?: return "pages/manager/promocodes/add"

        repository.save(PromoCodeDto.fromModel(promoCode))
        return "redirect:/manager/promocodes"
    }

    @GetMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val promoCode = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/promocodes"
        model.addAttribute("id", id)
        model.addAttribute("promoCode", PromoCodeForm.fromModel(promoCode))
        return "pages/manager/promocodes/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("promoCode") promoCodeForm: PromoCodeForm,
        result: BindingResult,
        model: Model,
    ): String {
        model.addAttribute("id", id)

        val promoCode = checkPromoCode(
            editing = true,
            promoCodeForm = promoCodeForm,
            result = result
        ) ?: return "pages/manager/promocodes/add"

        repository.save(PromoCodeDto.fromModel(promoCode))
        return "redirect:/manager/promocodes"
    }

    private fun checkPromoCode(
        editing: Boolean = false,
        promoCodeForm: PromoCodeForm,
        result: BindingResult,
    ): PromoCode? {
        if (result.hasErrors()) {
            return null
        }

        return checkForCode(
            compareIds = editing,
            promoCodeForm = promoCodeForm,
            result = result
        )
    }

    private fun checkForCode(
        compareIds: Boolean = false,
        promoCodeForm: PromoCodeForm,
        result: BindingResult,
    ): PromoCode? {
        val promoCode = promoCodeForm.toModel()
        if (
            with(repository.findByCodeIgnoreCase(promoCode.code)) {
                this != null && if (compareIds) {
                    this.id != promoCode.id
                } else {
                    true
                }
            }
        ) {
            result.rejectValue("code", "error.existing_code", "Этот код уже существует")
            return null
        }

        return promoCode
    }

    @PostMapping("/toggle/{id}")
    fun toggle(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val promoCode = repository.findByIdOrNull(id)
        if (promoCode != null) {
            repository.save(
                promoCode.copy(
                    active = !promoCode.active
                )
            )
        }
        return "redirect:/manager/promocodes"
    }
}