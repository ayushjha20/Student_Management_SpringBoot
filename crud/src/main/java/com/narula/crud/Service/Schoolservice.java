package com.narula.crud.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.narula.crud.DtoRequest.SchoolResq;
import com.narula.crud.DtoResponse.SchoolResp;
import com.narula.crud.Entity.School;
import com.narula.crud.Exception.ResourcenotfounndException;
import com.narula.crud.Repository.Schoolrepository;

@Service
public class Schoolservice {
    private Schoolrepository repo;

    public Schoolservice(Schoolrepository repo) {
        this.repo = repo;
    }

    // create
    public SchoolResp Create(SchoolResq resq) {
        School student = DtotoEntity(resq);
        School st = repo.save(student);
        return EntitytoDto(st);
    }

    // Read
    public SchoolResp Read(Long id) {
        School st = repo.findById(id)
                .orElseThrow(() -> new ResourcenotfounndException("Student resource not found on id: "+id));
        return EntitytoDto(st);

    }

    // Readall
    public List<SchoolResp> Readall() {
        return repo.findAll()
                .stream()
                .map(this::EntitytoDto)
                .toList();
    }

    // update
    public SchoolResp Update(long id, SchoolResq resq) {
        School list = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student id not found!!!"));

        list.setName(resq.getName());
        list.setRoll_No(resq.getRoll_No());
        list.setCourse(resq.getCourse());
        list.setMarks(resq.getMarks());
        list.setCourse(resq.getCourse());

        repo.save(list);

        return EntitytoDto(list);
    }

    // delete
    public SchoolResp Delete(Long id) {
        School list = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Id not found!!!"));

        repo.delete(list); // it is used to delete the id after finding it

        return EntitytoDto(list);

    }

    // Entity → Response DTO
    public SchoolResp EntitytoDto(School student) {

        SchoolResp resp = new SchoolResp();
        resp.setId(student.getId());
        resp.setName(student.getName());
        resp.setRoll_No(student.getRoll_No());
        resp.setMarks(student.getMarks());
        resp.setCourse(student.getCourse());

        return resp;
    }

    // Request DTO → Entity
    public School DtotoEntity(SchoolResq resp) {

        School student = new School();

        student.setName(resp.getName());
        student.setRoll_No(resp.getRoll_No());
        student.setMarks(resp.getMarks());
        student.setCourse(resp.getCourse());

        return student;
    }

}
