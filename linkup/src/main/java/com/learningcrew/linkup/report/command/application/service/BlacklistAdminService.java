package com.learningcrew.linkup.report.command.application.service;

import com.learningcrew.linkup.report.command.application.dto.request.BlacklistRegisterRequest;
import com.learningcrew.linkup.report.command.application.dto.response.BlacklistRegisterResponse;
import com.learningcrew.linkup.report.command.application.dto.response.BlacklistRemoveResponse;

public interface BlacklistAdminService {

    BlacklistRegisterResponse registerBlacklist(BlacklistRegisterRequest request);

    BlacklistRemoveResponse removeBlacklist(Integer memberId);
}
