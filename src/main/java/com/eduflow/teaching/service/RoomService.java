package com.eduflow.teaching.service;


import com.eduflow.teaching.domain.Room;
import com.eduflow.teaching.repo.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    public Room create(Room room) {
        if (room == null) throw new IllegalArgumentException("Room is required");

        String code = normalizeCode(room.getCode());
        if (code.isBlank()) throw new IllegalArgumentException("Room code is required");

        if (roomRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalStateException("Room code already exists: " + code);
        }

        room.setCode(code);
        if (room.getCapacity() != null && room.getCapacity() < 0) {
            throw new IllegalArgumentException("Capacity must be >= 0");
        }
        // par défaut active = true si null (selon ton modèle)
        // room.setActive(true);

        return roomRepository.save(room);
    }

    public Room update(String roomId, Room patch) {
        Room existing = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));

        if (patch.getCode() != null) {
            String code = normalizeCode(patch.getCode());
            if (code.isBlank()) throw new IllegalArgumentException("Room code is required");

            boolean codeChanged = !existing.getCode().equalsIgnoreCase(code);
            if (codeChanged && roomRepository.existsByCodeIgnoreCase(code)) {
                throw new IllegalStateException("Room code already exists: " + code);
            }
            existing.setCode(code);
        }

        if (patch.getBuilding() != null) existing.setBuilding(patch.getBuilding().trim());
        if (patch.getFloor() != null) existing.setFloor(patch.getFloor());
        if (patch.getCapacity() != null) {
            if (patch.getCapacity() < 0) throw new IllegalArgumentException("Capacity must be >= 0");
            existing.setCapacity(patch.getCapacity());
        }
        if (patch.getType() != null) existing.setType(patch.getType());
        // si tu veux permettre active toggle
        existing.setActive(patch.isActive());

        return roomRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public Room getById(String roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
    }

    @Transactional(readOnly = true)
    public List<Room> listAll() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Room> listActive() {
        return roomRepository.findByActiveTrueOrderByCodeAsc();
    }

    public void delete(String roomId) {
        // option : interdire suppression si des sessions existent (je te le mets plus bas)
        roomRepository.deleteById(roomId);
    }

    private String normalizeCode(String code) {
        return code == null ? "" : code.trim().toUpperCase();
    }
}