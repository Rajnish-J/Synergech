package com.projectDemo1.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectDemo1.DTO.patientDTO;
import com.projectDemo1.Entity.appointmentsVO;
import com.projectDemo1.Entity.patientVO;
import com.projectDemo1.Response.ResponseHandle;
import com.projectDemo1.Service.patientService;
import com.projectDemo1.customExceptions.AppointmentException;
import com.projectDemo1.customExceptions.EmailException;
import com.projectDemo1.customExceptions.IdException;
import com.projectDemo1.customExceptions.PasswordException;
import com.projectDemo1.customExceptions.PhoneNumberException;
import com.projectDemo1.customExceptions.patientException;

@RestController
@RequestMapping("/patient")
public class patientController {

	@Autowired
	private patientService pservice;

	@Autowired
	private ResponseHandle res;

	// insert:
	@PostMapping("/insert")
	public ResponseEntity<?> insertPatient(@RequestBody patientDTO dto) {

		// converting DTO to entity
		patientVO vo = new patientVO();
		vo.setFirstName(dto.getFirstName());
		vo.setLastName(dto.getLastName());
		vo.setPatientEmail(dto.getPatientEmail());
		vo.setPatientPassword(dto.getPatientPassword());
		vo.setPatientPhone(dto.getPatientPhone());
		vo.setDob(dto.getDob());

		try {
			res = pservice.insertPatientDetails(vo);
			dto.setUpdatedAt(res.getPatient().getUpdatedAt());
			dto.setCreatedAt(res.getPatient().getCreatedAt());
			dto.setPatientId(res.getPatient().getPatientId());
			return ResponseEntity.ok("Patient Details successfully save" + res.getPatient().getPatientId());
		} catch (patientException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PhoneNumberException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (EmailException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PasswordException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// fetch by id:
	@GetMapping("patientId/{id}")
	public ResponseEntity<?> findBypatientId(@PathVariable("id") long id) {
		try {
			res = pservice.fetchById(id);
			return ResponseEntity.ok("Patient Details Fetched by ID:" + res.getPatient());
		} catch (IdException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// done
	// fetch all:
	@GetMapping("/fetchallPatient")
	public List<patientDTO> fetchall() {
		res = pservice.fetchAll();
		List<patientVO> list = res.getListpatient();
		List<patientDTO> listd = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			patientVO vo = list.get(i);
			patientDTO getDto = mapToDTO(vo);
			listd.add(getDto);
		}

		return listd;

	}

	// done
	// update method
	@PutMapping("/update/{id}")
	public ResponseEntity<?> fetchpatientDetails(@PathVariable long id) {
		try {
			res = pservice.updatePatientDetails(id);
			return ResponseEntity.ok(mapToDTO(res.getPatient()));
		} catch (IdException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// association method
	@PostMapping("/associatePatientsWithAppointments")
	public ResponseEntity<?> associate(@RequestBody patientDTO dto) {
		patientVO vo = new patientVO();
		vo.setFirstName(dto.getFirstName());
		vo.setLastName(dto.getLastName());
		vo.setPatientEmail(dto.getPatientEmail());
		vo.setPatientPassword(dto.getPatientPassword());
		vo.setPatientPhone(dto.getPatientPhone());
		vo.setDob(dto.getDob());

		List<appointmentsVO> list = new ArrayList<>();
		for (appointmentsVO obj : dto.getAppointments()) {
			appointmentsVO avo = new appointmentsVO();
			avo.setAppointmentDate(obj.getAppointmentDate());
			avo.setReason(obj.getReason());
			avo.setDoctorId(obj.getDoctorId());
			System.out.println(avo);
			avo.setPatient(vo);
			list.add(avo);
		}
		vo.setAppointments(list);
		try {
			res = pservice.associate(vo);
			return ResponseEntity
					.ok("Patient Details and Appointments added successfully" + res.getPatient().getPatientId());
		} catch (patientException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PhoneNumberException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (EmailException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (PasswordException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (AppointmentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// done
	// find by patient phone number:
	@GetMapping("/fetchByPhoneNumber/{ph}")
	public ResponseEntity<?> findbyphone(@PathVariable("ph") String ph) {
		try {
			res = pservice.findbyphone(ph);
			return ResponseEntity.ok("Patient Details Fetched by ID:");
		} catch (PhoneNumberException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	// done
	// fetch by day appointments:
	@GetMapping("/appointmentDate/{td}")
	public List<patientDTO> findapptDay(@PathVariable("td") LocalDate td) {
		res = pservice.findapptDay(td);
		List<patientVO> list = res.getListpatient();
		List<patientDTO> listd = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			patientVO vo = list.get(i);
			patientDTO getDto = mapToDTO(vo);
			listd.add(getDto);
		}

		return listd;
	}

	// done
	// fetch by more appointments
	@GetMapping("/findAppointmentsById/{id}")
	public List<patientDTO> findAppointmentsByNumber(@PathVariable("id") long n) {
		res = pservice.findAppointmentsByNumber(n);
		List<patientVO> list = res.getListpatient();
		List<patientDTO> listd = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			patientVO vo = list.get(i);
			patientDTO getDto = mapToDTO(vo);
			listd.add(getDto);
		}

		return listd;
	}

	// done
	// fetch first name and last name:
	@GetMapping("/findFirstandLastNamebyPatientId/{id}")
	public String findName(@PathVariable("id") long n) {
		res = pservice.findName(n);
		return "First name: " + res.getPro().getFirstName() + " Second name: " + res.getPro().getLastName();
	}

	// done
	// Appointment by between two days:
	@GetMapping("/patientDetailsAmongTwoDate/{sd}/{ld}")
	public List<patientDTO> betweenTwoDOBpat(@PathVariable("sd") LocalDate sd, @PathVariable("ld") LocalDate ld) {
		res = pservice.betweenTwoDOBpat(sd, ld);
		List<patientVO> list = res.getListpatient();
		List<patientDTO> listd = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			patientVO vo = list.get(i);
			patientDTO getDto = mapToDTO(vo);
			listd.add(getDto);
		}

		return listd;

	}

	// done
	// ascending order:
	@GetMapping("/AscendingOrder")
	public List<patientDTO> acending() {
		res = pservice.acending();
		List<patientVO> list = res.getListpatient();
		List<patientDTO> listd = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			patientVO vo = list.get(i);
			patientDTO getDto = mapToDTO(vo);
			listd.add(getDto);
		}

		return listd;

	}

	// own method => converts entity to DTO:
	public static patientDTO mapToDTO(patientVO vo) {

		patientDTO dto = new patientDTO();
		dto.setFirstName(vo.getFirstName());
		dto.setLastName(vo.getLastName());
		dto.setPatientEmail(vo.getPatientEmail());
		dto.setPatientPassword(vo.getPatientPassword());
		dto.setPatientPhone(vo.getPatientPhone());
		dto.setDob(vo.getDob());
		dto.setUpdatedAt(vo.getUpdatedAt());
		dto.setCreatedAt(vo.getCreatedAt());
		dto.setPatientId(vo.getPatientId());

		return dto;

	}
}
