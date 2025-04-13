package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.report.command.application.dto.response.ObjectionDecisionResponse;

public interface ObjectionDecisionService {

    ObjectionDecisionResponse acceptObjection(Long objectionId);
    ObjectionDecisionResponse rejectObjection(Long objectionId);
}
