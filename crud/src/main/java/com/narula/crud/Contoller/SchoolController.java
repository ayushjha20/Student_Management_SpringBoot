package com.narula.crud.Contoller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narula.crud.DtoRequest.SchoolResq;
import com.narula.crud.DtoResponse.SchoolResp;
import com.narula.crud.Service.Schoolservice;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/School")
public class SchoolController {

    private Schoolservice service;

    public SchoolController(Schoolservice service) {
        this.service = service;
    }

    // create
    @PostMapping
    public ResponseEntity<SchoolResp> Create(@Valid @RequestBody SchoolResq resq) {
        SchoolResp resp = service.Create(resq);
        return ResponseEntity.ok(resp);

    }

    // read
    @GetMapping("/{id}")
    public ResponseEntity<SchoolResp> Read(@PathVariable Long id) {
        SchoolResp read = service.Read(id);
        return ResponseEntity.ok(read);
    }

    // readall
    @GetMapping("/readall")
    public ResponseEntity<List<SchoolResp>> Readall() {
        List<SchoolResp> list = service.Readall();
        return ResponseEntity.ok(list);

    }

    // update
    @PutMapping("/update/{id}")
    public ResponseEntity<SchoolResp> Update(@PathVariable Long id,
            @RequestBody SchoolResq resq) {
        SchoolResp existedlist = service.Update(id, resq);
        return ResponseEntity.ok(existedlist);
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> Delete(@PathVariable Long id) {
        service.Delete(id);
        return ResponseEntity.noContent().build();
    }

}
