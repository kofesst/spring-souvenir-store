package me.kofesst.spring.souvenirstore.controller.manager

import me.kofesst.spring.souvenirstore.database.PickupPointDto
import me.kofesst.spring.souvenirstore.model.PickupPoint
import me.kofesst.spring.souvenirstore.model.form.PickupPointForm
import me.kofesst.spring.souvenirstore.repository.PickupPointsRepository
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
@RequestMapping("/manager/points")
@PreAuthorize("hasAnyAuthority('Manager')")
class ManagerPointsController @Autowired constructor(
    private val repository: PickupPointsRepository,
) {
    @GetMapping
    fun overview(
        @RequestParam("query") query: String?,
        model: Model,
    ): String {
        val points = repository.findAll().asModels().run {
            if (query?.isNotBlank() == true) {
                filter { point ->
                    model.addAttribute("query", query)
                    point.address.lowercase().contains(query.lowercase())
                }
            } else {
                this
            }
        }

        model.addAttribute("models", points)
        return "pages/manager/points/overview"
    }

    @GetMapping("/add")
    fun add(
        pointForm: PickupPointForm,
        model: Model,
    ): String {
        model.addAttribute("point", pointForm)
        return "pages/manager/points/add"
    }

    @PostMapping("/add")
    fun add(
        @Valid @ModelAttribute("point") pointForm: PickupPointForm,
        result: BindingResult,
        model: Model,
    ): String {
        val point = checkPoint(
            pointForm = pointForm,
            result = result
        ) ?: return "pages/manager/points/add"

        repository.save(PickupPointDto.fromModel(point))
        return "redirect:/manager/points"
    }

    @GetMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        val point = repository.findByIdOrNull(id)?.toModel() ?: return "redirect:/manager/points"
        model.addAttribute("id", id)
        model.addAttribute("point", PickupPointForm.fromModel(point))
        return "pages/manager/points/add"
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute("point") pointForm: PickupPointForm,
        result: BindingResult,
        model: Model,
    ): String {
        model.addAttribute("id", id)

        val point = checkPoint(
            editing = true,
            pointForm = pointForm,
            result = result
        ) ?: return "pages/manager/points/add"

        repository.save(PickupPointDto.fromModel(point))
        return "redirect:/manager/points"
    }

    private fun checkPoint(
        editing: Boolean = false,
        pointForm: PickupPointForm,
        result: BindingResult,
    ): PickupPoint? {
        if (result.hasErrors()) {
            return null
        }

        return checkForPointAddress(
            compareIds = editing,
            pointForm = pointForm,
            result = result
        )
    }

    private fun checkForPointAddress(
        compareIds: Boolean = false,
        pointForm: PickupPointForm,
        result: BindingResult,
    ): PickupPoint? {
        val point = pointForm.toModel()
        if (
            with(repository.findByAddressIgnoreCase(point.address)) {
                this != null && if (compareIds) {
                    this.id != point.id
                } else {
                    true
                }
            }
        ) {
            result.rejectValue("address", "error.existing_address", "Этот адрес уже используется")
            return null
        }

        return point
    }

    @PostMapping("/delete/{id}")
    fun delete(
        @PathVariable("id") id: Long,
        model: Model,
    ): String {
        repository.deleteById(id)
        return "redirect:/manager/points"
    }
}