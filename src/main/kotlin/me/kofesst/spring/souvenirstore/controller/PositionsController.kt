package me.kofesst.spring.souvenirstore.controller

import me.kofesst.spring.souvenirstore.model.UserRole
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/positions")
class PositionsController {
    @GetMapping
    fun overview(model: Model): String {
        val positions = UserRole.positions()
        model.addAttribute("models", positions)
        return "positions/overview"
    }

    @PostMapping
    fun search(
        @RequestParam(name = "query") query: String,
        model: Model,
    ): String {
        if (query.isBlank()) {
            return "redirect:/positions"
        }

        val positions = UserRole.positions().filter { position ->
            position.displayName.lowercase().contains(query.lowercase())
        }
        model.addAttribute("models", positions)
        model.addAttribute("query", query)
        return "positions/overview"
    }
}