package ru.gisback.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.gisback.dto.CreateObjectGeometryDTO;
import ru.gisback.dto.LayerDTO;
import ru.gisback.dto.ObjectGeometryDTO;
import ru.gisback.model.User;
import ru.gisback.repositories.UserRepo;
import ru.gisback.services.LayerService;
import ru.gisback.services.ObjectGeometryService;

import java.util.List;

@RestController
@RequestMapping("/api/layer")
@RequiredArgsConstructor
public class LayerController {

    private final LayerService layerService;
    private final ObjectGeometryService objectService;
    private final UserRepo userRepository;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<LayerDTO> createLayer(@RequestBody LayerDTO dto){
        LayerDTO saved = layerService.createLayer(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/get-layer-by-access/me")
    public ResponseEntity<List<LayerDTO>> getLayerByAccessForMe() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return ResponseEntity.ok(layerService.getAccessibleLayers(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<LayerDTO>> getAllLayers() {
        try{
            return ResponseEntity.ok(layerService.getAllLayers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{layerId}/objects")
    public ResponseEntity<List<ObjectGeometryDTO>> getLayerObjects(@PathVariable Long layerId) {
        return ResponseEntity.ok(layerService.getLayerObjects(layerId));
    }

    @PostMapping("/{layerId}/objects")
    public ResponseEntity<ObjectGeometryDTO> createObject(
            @PathVariable Long layerId,
            @Valid @RequestBody CreateObjectGeometryDTO dto
    ) {
        return ResponseEntity.ok(objectService.addObject(layerId, dto));
    }

    @DeleteMapping("/{layerId}/objects/{objId}")
    public ResponseEntity<Void> deleteObject(@PathVariable Long layerId,
                                             @PathVariable Long objId) {
        objectService.deleteObject(layerId, objId);
        return ResponseEntity.noContent().build();
    }
}

