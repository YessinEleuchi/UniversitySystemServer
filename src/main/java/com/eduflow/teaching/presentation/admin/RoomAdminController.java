package com.eduflow.teaching.presentation.admin;

import com.eduflow.teaching.domain.Room;
import com.eduflow.teaching.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class RoomAdminController {

    private final RoomService roomService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomService.listAll());
        model.addAttribute("room", new Room()); // form create
        return "admin/teaching/rooms/list";
    }

    @PostMapping
    public String create(@ModelAttribute Room room) {
        roomService.create(room);
        return "redirect:/admin/rooms";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id, @ModelAttribute Room room) {
        roomService.update(id, room);
        return "redirect:/admin/rooms";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        roomService.delete(id);
        return "redirect:/admin/rooms";
    }
}
