package com.clinica.facade.dto;

import com.clinica.facade.entity.Cita;
import com.clinica.facade.entity.Consulta;
import com.clinica.facade.entity.Prescripcion;
import com.clinica.facade.entity.ResultadoLaboratorio;
import java.util.List;

public class CompleteHistory {
    private String patientId;
    private String patientName;
    private List<Cita> pastAppointments;
    private List<Prescripcion> prescriptions;
    private List<ResultadoLaboratorio> laboratoryResults;
    private List<Consulta> consultations;

    public CompleteHistory() {}

    public CompleteHistory(String patientId, String patientName, List<Cita> pastAppointments,
                           List<Prescripcion> prescriptions, List<ResultadoLaboratorio> laboratoryResults,
                           List<Consulta> consultations) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.pastAppointments = pastAppointments;
        this.prescriptions = prescriptions;
        this.laboratoryResults = laboratoryResults;
        this.consultations = consultations;
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public List<Cita> getPastAppointments() { return pastAppointments; }
    public void setPastAppointments(List<Cita> pastAppointments) { this.pastAppointments = pastAppointments; }
    public List<Prescripcion> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<Prescripcion> prescriptions) { this.prescriptions = prescriptions; }
    public List<ResultadoLaboratorio> getLaboratoryResults() { return laboratoryResults; }
    public void setLaboratoryResults(List<ResultadoLaboratorio> laboratoryResults) { this.laboratoryResults = laboratoryResults; }
    public List<Consulta> getConsultations() { return consultations; }
    public void setConsultations(List<Consulta> consultations) { this.consultations = consultations; }
}