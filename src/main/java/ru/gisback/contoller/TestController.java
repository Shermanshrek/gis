package ru.gisback.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.gisback.model.LayerModel;
import ru.gisback.services.TestService;

import java.util.List;

@RestController
public class TestController {
    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test")
    public ResponseEntity test(){
        try {
            testService.test();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/test/get-all")
    public ResponseEntity<List<LayerModel>> testGetAll(){
        try {
            return ResponseEntity.ok(testService.getLayers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/test/get-layer-by-access/{id}")
    public ResponseEntity<List<LayerModel>> getLayerByAccess(@PathVariable Long id){
        try {
            return ResponseEntity.ok(testService.getAccessibleLayers(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
