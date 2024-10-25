package com.Check;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

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
import com.Check.Entity.DoctorVO;
import com.Check.Entity.PatientVO;
import com.Check.Repo.DoctorDetailsProjection;
import com.Check.Response.ResponseHandle;
import com.Check.Response.ResponseHandleAppointments;
import com.Check.Response.ResponseHandleDoctor;
import com.Check.Service.AppointmentsService;
import com.Check.Service.DoctorService;
import com.Check.Service.PatientService;

@SpringBootApplication
@EnableJpaAuditing
public class CheckApplication {

	@Autowired
	public PatientService pService;

	@Autowired
	private AppointmentsService aService;

	@Autowired
	private ResponseHandle response;

	@Autowired
	private ResponseHandleAppointments resAppt;

	@Autowired
	private DoctorService dser;

	@Autowired
	private ResponseHandleDoctor docRes;

	static Logger log = Logger.getLogger(CheckApplication.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CheckApplication.class, args);
		Scanner sc = new Scanner(System.in);

		CheckApplication ref = ctx.getBean(CheckApplication.class);

		PropertyConfigurator.configure(
				"C:\\Users\\Lenovo\\OneDrive\\Desktop\\GIT\\Hospital-Appointment-Scheduling\\HospitalAppointmentScheduling\\src\\main\\java\\log4j\\log4j.properities");
		log.info(" Application Started Started..");

		boolean mainRepeat = true;
		log.info("Patient chooses create account option...");
		do {
			System.out.println("1. Patient menu\n2. Appointments menu\n3. Exit");
			System.out.print("Enter the option: ");
			int mainOption = sc.nextInt();

			switch (mainOption) {
			case 1: {
				boolean patientRepeat = true;
				do {
					System.out.println(
							"1. Save Patient\n2. FindByID\n3. FetchAllPatients\n4. Update Details\n5. Associate\n6. "
									+ "Fetch patient by phone number\n7. fetch appointments by the date\n8. "
									+ "Find first and last name by patient ID\n9. Fetch all the patient details among the two date\n10. Get the patients in "
									+ "Ascending order\n11. exit");
					System.out.print("Enter the option: ");
					int patientOption = sc.nextInt();
					switch (patientOption) {
					case 1: {
						ref.insertPatient();
						break;
					}
					case 2: {
						System.out.print("Enter the patient ID: ");
						long id = sc.nextLong();
						ref.fetchByIDPatient(id);
						break;
					}
					case 3: {
						ref.fetchAllPatients();
						break;
					}
					case 4: {
						System.out.print("Enter the patient ID: ");
						int id = sc.nextInt();
						ref.updatePatient(id);
						break;
					}
					case 5: {
						ref.associatePatientWithAppointment();
						System.out.println("Patient Details Fetched by ID:");
						break;
					}
					case 6: {
						System.out.print("Enter the patient phone number to fetch patient Details: ");
						String ph = sc.next();
						ref.fetchbyPatientPhone(ph);
						break;
					}
					case 7: {
						ref.fetchapptday();
						break;
					}
					case 8: {
						System.out.println("Enter the patient ID: ");
						long id = sc.nextLong();
						ref.findPatientName(id);
						break;
					}
					case 9: {
						System.out.print("Enter the Start Date in the format (YYYY-MM-DD): ");
						String startDate = sc.next();
						System.out.print("Enter the End Date in the format (YYYY-MM-DD): ");
						String endDate = sc.next();
						DateTimeFormatter formatAppt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDate startedDate = LocalDate.parse(startDate, formatAppt);
						LocalDate endedDate = LocalDate.parse(endDate, formatAppt);

						ref.betweenTwoDOBpat(startedDate, endedDate);
						break;

					}
					case 10: {
						ref.ascendingPatient();
						break;

					}
					case 11: {
						log.info("patient chooses to EXIT the application...");
						patientRepeat = false;
						System.out.println("Thank you for Using patient page returning to main page");
						break;
					}
					default: {
						System.out.println("Enter the correct option");
					}
					}
				} while (patientRepeat);
				break;
			}
			case 2: {
				boolean appointmentRepeat = true;
				do {
					System.out.println(
							"1. Add appointments with creating patient account\n2. Add appointments with already registered patient ID\n3. "
									+ "Fetch appointments by ID\n4. fetch all appointments\n5. Update appointmens status\n6. Fetch appointments in two dates"
									+ "\n7. Fetch appointments in ascending order" + "\n8. EXIT");
					System.out.print("Enter the option: ");
					int appointmentOption = sc.nextInt();
					switch (appointmentOption) {
					case 1: {
						ref.insertApptWithPatientAcc();
						break;
					}
					case 2: {
						ref.insertAppointmentsWithPatientID();
						break;
					}
					case 3: {
						System.out.print("Enter the Appointment ID: ");
						long id = sc.nextLong();
						ref.fetchByIDAppointment(id);
						break;
					}
					case 4: {
						ref.fetchAllAppointments();
						break;
					}
					case 5: {
						System.out.print("Enter the Appointment ID: ");
						long id = sc.nextLong();
						ref.updateAppointment(id);
						break;
					}
					case 6: {
						System.out.print("Enter the Start Date in the format (YYYY-MM-DD): ");
						String startDate = sc.next();
						System.out.print("Enter the End Date in the format (YYYY-MM-DD): ");
						String endDate = sc.next();
						DateTimeFormatter formatAppt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDate startedDate = LocalDate.parse(startDate, formatAppt);
						LocalDate endedDate = LocalDate.parse(endDate, formatAppt);

						ref.fetchApptBetweenTwoDates(startedDate, endedDate);
						break;
					}
					case 7: {
						ref.ascendingAppointments();
						break;
					}
					case 8: {
						System.out.println("Thank you for using Appointment booking page, retuning to main page");
						appointmentRepeat = false;
						break;
					}
					}
				} while (appointmentRepeat);
				break;
			}
			case 3: {
				System.out.println("Thank you for using Hospital Management System application");
				break;
			}
			default: {
				System.out.println("Enter the correct option");
			}
			}
		} while (mainRepeat);
	}
	// ------------------------------------------------------------------------------------------------------------------------------//

	// insert method
	public void insertPatient() {
		Scanner sc = new Scanner(System.in);
		PatientVO patient = new PatientVO();

		System.out.print("Enter the First Name: ");
		patient.setFirstName(sc.next());

		System.out.print("Enter the Last Name: ");
		patient.setLastName(sc.next());

		System.out.print("Enter the phone number: ");
		patient.setPatientPhone(sc.next());

		System.out.print("Enter the Date of birth in the format (YYYY-MM-DD): ");
		String date = sc.next();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dob = LocalDate.parse(date, format);
		patient.setDob(dob);

		System.out.print("Enter the Email: ");
		patient.setPatientEmail(sc.next());

		System.out.print("Enter the Password: ");
		patient.setPatientPassword(sc.next());

		try {
			response = pService.insertPatientDetails(patient);
		} catch (PatientException e) {
			System.err.println(e.getMessage());
		} catch (PhoneNumberException e) {
			System.err.println(e.getMessage());
		} catch (EmailException e) {
			System.err.println(e.getMessage());
		} catch (PasswordException e) {
			System.err.println(e.getMessage());
		} catch (DateOfBirthException e) {
			System.err.println(e.getMessage());
		}

		if (response.getPatient().getPatientId() > 0) {
			System.out.println("Your Generated Patient ID is: " + response.getPatient().getPatientId());

		} else {
			System.out.println("Failed");
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// fetch by ID:
	public void fetchByIDPatient(long id) {
		try {
			response = pService.fetchById(id);
			if (response.getSucessMessage() != null) {
				System.out.println(response.getSucessMessage() + response.getId());
				System.out.println(response.getPatient());
			}
		} catch (IdException e) {
			System.out.println(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// fetch all method:
	public void fetchAllPatients() {
		response = pService.fetchAll();
		List<PatientVO> patientlist = response.getListPatient();
		for (PatientVO obj : patientlist) {
			System.out.println(obj);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// update method:
	public void updatePatient(long id) {
		try {
			response = pService.updatePatientDetails(id);
			if (response.getSucessMessage() != null) {
				System.out.println(response.getSucessMessage() + response.getId());
			}
		} catch (IdException e) {
			System.err.println(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// associate method:
	public void associatePatientWithAppointment() {

		Scanner sc = new Scanner(System.in);
		PatientVO patient = new PatientVO();

		System.out.print("Enter the First Name: ");
		patient.setFirstName(sc.next());

		System.out.print("Enter the Last Name: ");
		patient.setLastName(sc.next());

		System.out.print("Enter the phone number: ");
		patient.setPatientPhone(sc.next());

		System.out.print("Enter the Date of birth in the format (YYYY-MM-DD): ");
		String date = sc.next();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dob = LocalDate.parse(date, format);
		patient.setDob(dob);

		System.out.print("Enter the Email: ");
		patient.setPatientEmail(sc.next());

		System.out.print("Enter the Password: ");
		patient.setPatientPassword(sc.next());

		System.out.println("Enter Number of Appointments Details :");
		int n = sc.nextInt();

		List<AppointmentsVO> list = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			AppointmentsVO appt = new AppointmentsVO();

			System.out.print("Enter the Date of Appointment in the format (YYYY-MM-DD): ");
			String apptDate = sc.next();
			DateTimeFormatter formatAppt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate appntDate = LocalDate.parse(apptDate, formatAppt);
			appt.setAppointmentDate(appntDate);

			sc.nextLine();
			System.out.println("Enter the reason: ");
			appt.setReason(sc.nextLine());

			System.out.println("Enter the doctor ID: ");
			DoctorVO dVO = new DoctorVO();
			dVO.setDoctorId(1l);
			appt.setDoctor(dVO);

			appt.setPatient(patient);
			list.add(appt);
		}

		patient.setAppointments(list);
		try {
			response = pService.associate(patient);
		} catch (PatientException e) {
			System.err.println(e.getMessage());
		} catch (PhoneNumberException e) {
			System.err.println(e.getMessage());
		} catch (EmailException e) {
			System.err.println(e.getMessage());
		} catch (PasswordException e) {
			System.err.println(e.getMessage());
		} catch (AppointmentException e) {
			System.err.println(e.getMessage());
		} catch (AppointmentBookingDateException e) {
			System.err.println(e.getMessage());
		} catch (DateOfBirthException e) {
			System.err.println(e.getMessage());
		}

		long id = response.getId();

		if (id > 0) {
			System.out.println(response.getSucessMessage());
		} else {
			System.out.println(response.getFailureMessage());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// fetch by phone number
	public void fetchbyPatientPhone(String ph) {
		try {
			response = pService.findbyphone(ph);
			System.out.println(response.getPatient());
		} catch (PhoneNumberException e) {

			System.err.println(e.getMessage());
		}

	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// fetch by day appointments
	public void fetchapptday() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the date in the format (YYYY-MM-DD): ");
		String date = sc.next();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate day = LocalDate.parse(date, format);

		try {
			response = pService.findapptDay(day);
		} catch (AppointmentException e) {
			System.err.println(e.getMessage());
		}
		if (response.getSucessMessage() != null) {
			System.out.println(response.getListPatient());
		}
		sc.close();
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// fetch first name and last name:
	public void findPatientName(long id) {
		try {
			response = pService.findName(id);
			System.out.println("First name: " + response.getPro().getFirstName() + " Second name: "
					+ response.getPro().getLastName());
		} catch (IdException e) {
			System.err.println(e.getMessage());
		}

	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// Appointment by between two days:
	public void betweenTwoDOBpat(LocalDate sd, LocalDate ld) {
		try {
			response = pService.betweenTwoDOBpat(sd, ld);
		} catch (DateException e) {
			System.err.println(e.getMessage());
		}
		for (PatientVO obj : response.getListPatient()) {
			System.out.println(obj);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// ascending order:
	public void ascendingPatient() {
		try {
			response = pService.ascending();
		} catch (AppointmentException e) {
			System.err.println(e.getMessage());
		}
		for (PatientVO obj : response.getListPatient()) {
			System.out.println(obj);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// insert appointment with creating patient account
	public void insertApptWithPatientAcc() {
		Scanner sc = new Scanner(System.in);

		AppointmentsVO avo = new AppointmentsVO();

		System.out.print("Enter the appointment date in the format (YYYY-MM-DD): ");
		String date = sc.next();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate aDate = LocalDate.parse(date, format);
		avo.setAppointmentDate(aDate);

		sc.nextLine();

		System.out.print("Enter the reason: ");
		avo.setReason(sc.nextLine());

		System.out.print("Enter the doctor ID: ");
		DoctorVO dVO = new DoctorVO();
		dVO.setDoctorId(1l);
		avo.setDoctor(dVO);

		sc.nextLine();

		PatientVO patient = new PatientVO();
		System.out.print("Enter the First Name: ");
		patient.setFirstName(sc.next());

		System.out.print("Enter the Last Name: ");
		patient.setLastName(sc.next());

		System.out.print("Enter the phone number: ");
		patient.setPatientPhone(sc.next());

		System.out.print("Enter the Date of birth in the format (YYYY-MM-DD): ");
		String datePat = sc.next();
		DateTimeFormatter formatPat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dob = LocalDate.parse(datePat, formatPat);
		patient.setDob(dob);

		System.out.print("Enter the Email: ");
		patient.setPatientEmail(sc.next());

		System.out.print("Enter the Password: ");
		patient.setPatientPassword(sc.next());

		try {
			response = pService.insertPatientDetails(patient);
		} catch (PatientException e) {
			System.err.println(e.getMessage());
		} catch (PhoneNumberException e) {
			System.err.println(e.getMessage());
		} catch (EmailException e) {
			System.err.println(e.getMessage());
		} catch (PasswordException e) {
			System.err.println(e.getMessage());
		} catch (DateOfBirthException e) {
			System.err.println(e.getMessage());
		}

		avo.setPatient(response.getPatient());

		try {
			resAppt = aService.insertAppointments(avo);
		} catch (IdException e) {
			System.out.println(e.getMessage());
		} catch (EmailException e) {
			System.out.println(e.getMessage());
		} catch (PasswordException e) {
			System.out.println(e.getMessage());
		} catch (PhoneNumberException e) {
			System.out.println(e.getMessage());
		} catch (AppointmentException e) {
			System.out.println(e.getMessage());
		}

		if (resAppt.getAppoVo().getAppointmentID() > 0) {
			System.out.println("Your Generated Appointment ID is: " + resAppt.getAppoVo().getAppointmentID());

		} else {
			System.out.println("Failed");
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	public void insertAppointmentsWithPatientID() {
		Scanner sc = new Scanner(System.in);

		AppointmentsVO avo = new AppointmentsVO();

		System.out.print("Enter the appointment date in the format (YYYY-MM-DD): ");
		String date = sc.next();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate aDate = LocalDate.parse(date, format);
		avo.setAppointmentDate(aDate);

		sc.nextLine();

		System.out.print("Enter the reason: ");
		sc.nextLine();
		avo.setReason(sc.nextLine());

		resAppt = dser.fetch();
		for (DoctorDetailsProjection obj : resAppt.getListSpecialiaztion()) {
			System.out.println(obj.getDoctorId() + ", " + obj.getFirstName() + ", " + obj.getLastName() + ", "
					+ obj.getSpecialtyName());
		}

		System.out.print("Enter the doctor ID respect to the specilization: ");
		DoctorVO dVO = new DoctorVO();
		dVO.setDoctorId(sc.nextLong());
		try {
			docRes = dser.insertDoctorWithAppointment(dVO);
		} catch (IdException e) {
			System.out.println(e.getMessage());
		}
		avo.setDoctor(docRes.getDocVo());
		sc.nextLine();

		PatientVO patient = new PatientVO();
		System.out.print("Enter the already registerd Patient ID:");
		patient.setPatientId(sc.nextLong());
		avo.setPatient(patient);

		try {
			resAppt = aService.insertAppointmentsWithPatientID(avo);
		} catch (IdException e) {
			System.out.println(e.getMessage());
		} catch (EmailException e) {
			System.out.println(e.getMessage());
		} catch (PasswordException e) {
			System.out.println(e.getMessage());
		} catch (PhoneNumberException e) {
			System.out.println(e.getMessage());
		} catch (AppointmentException e) {
			System.out.println(e.getMessage());
		}

		if (resAppt.getAppoVo().getAppointmentID() > 0) {
			System.out.println("Your Generated Appointment ID is: " + resAppt.getAppoVo().getAppointmentID());

		} else {
			System.out.println("Failed");
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// fetch by ID:
	public void fetchByIDAppointment(long id) {
		try {
			resAppt = aService.fetchByID(id);
			if (response.getSucessMessage() != null) {
				System.out.println(resAppt.getSucessMessage() + resAppt.getAppoVo().getAppointmentID());
				System.out.println(resAppt.getAppoVo());
			} else {
				System.out.println("Not fetched");
			}
		} catch (IdException e) {
			System.out.println(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// fetch all method:
	public void fetchAllAppointments() {
		resAppt = aService.fetchAll();
		List<AppointmentsVO> appointmentlist = resAppt.getList();
		for (AppointmentsVO obj : appointmentlist) {
			System.out.println(obj);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// update method:
	public void updateAppointment(long id) {
		try {
			resAppt = aService.update(id);
			if (resAppt.getSucessMessage() != null) {
				System.out.println(resAppt.getSucessMessage() + resAppt.getAppoVo().getAppointmentID());
			}
		} catch (IdException e) {
			System.err.println(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	public void fetchApptBetweenTwoDates(LocalDate sd, LocalDate ld) {

		try {
			resAppt = aService.fetchApptBetweenTwoDates(sd, ld);
			if (resAppt.getSucessMessage() != null) {
				for (AppointmentsVO obj : resAppt.getList()) {
					System.out.println(obj);
				}
			}
		} catch (DateException e) {
			System.err.println(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

	// ascending order:
	public void ascendingAppointments() {
		try {
			resAppt = aService.acending();
		} catch (AppointmentException e) {
			System.err.println(e.getMessage());
		}
		for (AppointmentsVO obj : resAppt.getList()) {
			System.out.println(obj);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------//

}
