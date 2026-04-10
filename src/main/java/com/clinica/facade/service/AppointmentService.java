package com.clinica.facade.service;

import com.clinica.facade.entity.Cita;
import com.clinica.facade.entity.Medico;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final Map<String, Cita> appointments = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    private final List<Medico> doctors = Arrays.asList(
        new Medico("1", "Dr. Carlos Gomez", "Cardiology"),
        new Medico("2", "Dr. Maria Lopez", "Cardiology"),
        new Medico("3", "Dr. Pedro Sanchez", "General Medicine"),
        new Medico("4", "Dr. Ana Rodriguez", "Pediatrics"),
        new Medico("5", "Dr. Juan Martinez", "Dermatology")
    );

    public List<Medico> getDoctorsBySpecialty(String specialty) {
        return doctors.stream()
            .filter(m -> m.getSpecialty().equalsIgnoreCase(specialty))
            .collect(Collectors.toList());
    }

    public List<Medico> getAllDoctors() {
        return new ArrayList<>(doctors);
    }

    public List<LocalTime> getAvailableSchedules(String doctorId, LocalDateTime date) {
        List<LocalTime> schedules = Arrays.asList(
            LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0),
            LocalTime.of(11, 0), LocalTime.of(14, 0), LocalTime.of(15, 0),
            LocalTime.of(16, 0), LocalTime.of(17, 0)
        );
        int dayOfWeek = date.getDayOfWeek().getValue();
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            return Collections.emptyList();
        }
        List<LocalTime> occupied = appointments.values().stream()
            .filter(c -> c.getDoctorId().equals(doctorId) 
                && c.getDateTime().toLocalDate().equals(date.toLocalDate()))
            .map(c -> c.getDateTime().toLocalTime())
            .collect(Collectors.toList());
        return schedules.stream()
            .filter(h -> !occupied.contains(h))
            .collect(Collectors.toList());
    }

    public Cita schedule(String patientId, String patientName, String doctorId, 
                       String doctorName, String specialty, LocalDateTime dateTime) {
        String id = "APPT-" + idCounter.getAndIncrement();
        Cita appointment = new Cita(id, patientId, patientName, doctorId, doctorName, 
                            specialty, dateTime, "Scheduled");
        appointments.put(id, appointment);
        return appointment;
    }

    public boolean cancel(String appointmentId) {
        Cita appointment = appointments.get(appointmentId);
        if (appointment != null) {
            appointment.setStatus("Cancelled");
            return true;
        }
        return false;
    }

    public List<Cita> getPatientAppointments(String patientId) {
        return appointments.values().stream()
            .filter(c -> c.getPatientId().equals(patientId))
            .sorted(Comparator.comparing(Cita::getDateTime))
            .collect(Collectors.toList());
    }

    public List<Cita> getUpcomingAppointments(String patientId) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.values().stream()
            .filter(c -> c.getPatientId().equals(patientId) 
                && c.getDateTime().isAfter(now)
                && "Scheduled".equals(c.getStatus()))
            .sorted(Comparator.comparing(Cita::getDateTime))
            .limit(5)
            .collect(Collectors.toList());
    }
}