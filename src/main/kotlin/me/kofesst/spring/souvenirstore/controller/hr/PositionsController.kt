package me.kofesst.spring.souvenirstore.controller.hr

import me.kofesst.spring.souvenirstore.model.UserRole
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/hr/positions")
@PreAuthorize("hasAnyAuthority('HR')")
class PositionsController {
    @GetMapping
    fun overview(
        @RequestParam("query") query: String?,
        model: Model,
    ): String {
        val positions = UserRole.positions().run {
            if (query != null) {
                model.addAttribute("query", query)
                filter { position ->
                    position.displayName.lowercase().contains(query)
                }
            } else {
                this
            }
        }
        model.addAttribute("models", positions)
        return "pages/hr/positions/overview"
    }
}