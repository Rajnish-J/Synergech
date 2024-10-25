package com.Check.BO;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Check.CustomExceptions.AppointmentBookingDateException;
import com.Check.CustomExceptions.AppointmentException;
import com.Check.CustomExceptions.DateException;
import com.Check.CustomExceptions.DateOfBirthException;
import com.Check.CustomExceptions.EmailException;
import com.Check.CustomExceptions.IdException;
import com.Check.CustomExceptions.PasswordException;
import com.Check.CustomExceptions.PatientException;
import com.Check.CustomExceptions.PhoneNumberException;
import com.Check.Entity.AppointmentsVO;
import com.Check.Entity.PatientVO;
import com.Check.Repo.PatientProjection;
import com.Check.Repo.PatientRepo;

@Component
public class PatientBO {

	@Autowired
	PatientRepo patientRepo;

	// Insert method:
	public PatientVO insertPatientDetails(PatientVO vo)
			throws PatientException, PhoneNumberException, EmailException, PasswordException, DateOfBirthException {
		if (validatePatient(vo) && validateDOB(vo.getDob())) {
			patientRepo.save(vo);
			return vo;
		} else {
			throw new PatientException("Error in creating patient account..");
		}
	}

	// FindByID method:
	public PatientVO fetchByID(Long id) throws IdException {
		validateID(id);
		PatientVO ret = patientRepo.findById(id).get();
		return ret;

	}

	// fetchAll method:
	public List<PatientVO> fetchAll() {
		return patientRepo.findAll();
	}

	// update method:
	public PatientVO updatePatientDetails(long id) throws IdException {
		if (validateID(id)) {
			PatientVO vo = patientRepo.findById(id).get();
			vo.setLastName("Jai");
			vo = patientRepo.save(vo);
			return vo;
		}
		return null;
	}

	// associate method
	public PatientVO associate(PatientVO vo) throws PatientException, PhoneNumberException, EmailException,
			PasswordException, AppointmentException, AppointmentBookingDateException, DateOfBirthException {
		List<AppointmentsVO> appts = vo.getAppointments();
		for (AppointmentsVO obj : appts) {
			LocalDate d = obj.getAppointmentDate();
			validateAppointmentBookingDate(d);
		}

		if (validatePatient(vo) && validateAppointmentCount(vo) && validateDOB(vo.getDob())) {
			PatientVO insert = patientRepo.save(vo);
			return insert;
		}
		return null;
	}

	// fetch patient by phone number:
	public PatientVO fetchbyPhoneNumber(String ph) throws PhoneNumberException {
		if (validatePhoneNumber(ph) && checkPhOnDB(ph)) {
			PatientVO vo = patientRepo.findByPhoneNumber(ph);
			return vo;
		}
		return null;
	}

	// fetch by day appointments:
	public List<PatientVO> fetchapptDay(LocalDate td) throws AppointmentException {
		List<PatientVO> list = patientRepo.findPatientsWithAppointmentsDay(td);
		if (list.size() <= 0) {
			throw new AppointmentException("ERROR: there is no appointments in that date");
		}
		return list;
	}

	// fetch first name and last name:
	public PatientProjection findname(long n) throws IdException {
		PatientProjection po = null;
		if (validateID(n)) {
			po = patientRepo.findNameOfPatientById(n);
		}
		return po;

	}

	// Fetching all the patient details having the DOB between two days:
	public List<PatientVO> betweenTwoDOBpat(LocalDate sd, LocalDate ld) throws DateException {
		List<PatientVO> list = patientRepo.fetchBetweenDOBpat(sd, ld);
		if (sd.isAfter(ld)) {
			throw new DateException("start date could be before the end date");
		}
		return list;
	}

	// ascending order:
	public List<PatientVO> ascending() throws AppointmentException {
		List<PatientVO> list = patientRepo.fetchAscending();
		if (!(list.size() > 0)) {
			throw new AppointmentException("ERROR: There is no Records in the DataBase");
		}
		return list;
	}

	// validation methods:
	public boolean validatePhoneNumber(String phoneNumber) throws PhoneNumberException {
		if (phoneNumber == null || phoneNumber.length() != 10) {
			throw new PhoneNumberException("ERROR: The phone number lenght is atleast 10");
		}
		for (char c : phoneNumber.toCharArray()) {
			if (!Character.isDigit(c)) {
				throw new PhoneNumberException("ERROR: phone number does not have any alphabets");
			}
		}
		return true;
	}

	// checks for the phone number that exists in the database
	public boolean checkPhOnDB(String phoneNumber) throws PhoneNumberException {
		List<String> checkPhone = patientRepo.fetchPatientPhoneNumber();
		boolean contains = false;
		for (String obj : checkPhone) {
			if (phoneNumber.equals(obj)) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			throw new PhoneNumberException("ERROR: patient Phone number not exist in the database");
		}
		return true;

	}

	// validation for email
	public boolean validateEmail(String email) throws EmailException {
		if (email == null || email.isEmpty()) {
			throw new EmailException("ERROR: Email field could not be empty");
		}
		int atCount = 0;
		for (char c : email.toCharArray()) {
			if (c == '@') {
				atCount++;
				if (atCount == 1) {
					break;
				}
			}
		}
		if (atCount == 0) {
			throw new EmailException("ERROR: Email should contain atleast one " + "@" + " charactre in it");
		}
		return atCount == 1;
	}

	// validations for password
	public boolean validatePassword(String password) throws PatientException, PasswordException {

		if (password == null) {
			throw new PasswordException("ERROR: password field could not be empty");
		} else if (password.length() < 8 || password.length() > 12) {
			throw new PasswordException("ERROR: password length in between 8 to 12");
		}

		boolean hasUppercase = false;
		boolean hasLowercase = false;
		boolean hasDigit = false;
		boolean hasSpecial = false;

		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) {
				hasUppercase = true;
			} else if (Character.isLowerCase(c)) {
				hasLowercase = true;
			} else if (Character.isDigit(c)) {
				hasDigit = true;
			} else {
				hasSpecial = true;
			}
		}
		if (!(hasUppercase)) {
			throw new PasswordException("ERROR: password could have atleast one Uppercase letter");
		}
		if (!(hasLowercase)) {
			throw new PasswordException("ERROR: password could have atleast one Lowercase letter");
		}
		if (!(hasDigit)) {
			throw new PasswordException("ERROR: password could have atleast one Digit");
		}
		if (!(hasSpecial)) {
			throw new PasswordException("ERROR: password could have atleast one Special character");
		}

		return hasUppercase && hasLowercase && hasDigit && hasSpecial;
	}

	// checks the ID Checking
	public boolean validateID(Long id) throws IdException {
		List<Long> pID = patientRepo.fetchPatientId();

		boolean contains = false;
		for (Long obj : pID) {
			if (obj == id) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			throw new IdException("ERROR: patient ID not exist in the database");
		}
		if (id == null) {
			throw new IdException("ERROR: patient Id field could not be null");
		} else if (id <= 0) {
			throw new IdException("ERROR: patient ID could not be negative or zero");
		}

		return id != null && id > 0;
	}

	// checks if the list of appointments could be greater than zero
	public boolean validateAppointmentCount(PatientVO vo) throws AppointmentException {
		if (vo.getAppointments().size() <= 0) {
			throw new AppointmentException("ERROR: Appointments could not be zero..");
		}
		return vo.getAppointments().size() > 0;
	}

	// checks for the new appointments could not be in the past
	public boolean validateAppointmentBookingDate(LocalDate ld) throws AppointmentBookingDateException {

		LocalDate today = LocalDate.now();

		if (!(ld.isAfter(today) || ld.isEqual(today))) {
			throw new AppointmentBookingDateException("Appointment booking date could not be in the past");
		}
		return true;

	}

	// checks for the DOB could not be in the future
	public boolean validateDOB(LocalDate ld) throws DateOfBirthException {
		LocalDate today = LocalDate.now();

		if (!(ld.isBefore(today) || ld.isEqual(today))) {
			throw new DateOfBirthException("date of birth could not be in the future");
		}
		return true;
	}

	// Main validation method to validate a patient object:
	public boolean validatePatient(PatientVO vo)
			throws PatientException, PhoneNumberException, EmailException, PasswordException {
		return validatePhoneNumber(vo.getPatientPhone()) && validateEmail(vo.getPatientEmail())
				&& validatePassword(vo.getPatientPassword());
	}
}
