package com.learningcrew.linkup.common.infrastructure;

import com.learningcrew.linkup.common.dto.ApiResponse;
import com.learningcrew.linkup.common.dto.query.MeetingMemberDto;
import com.learningcrew.linkup.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "linkupuser", path = "/users", configuration = FeignClientConfig.class, fallback = MemberFeignFallback.class)
public interface MemberQueryClient {
    @GetMapping("/me/meetings/{memberId}")
    ApiResponse<MeetingMemberDto> getMemberById(@PathVariable("memberId") int memberId);

    @PostMapping("/{memberId}/manner-temperature")
    ApiResponse<Void> updateMannerTemperature(@PathVariable int memberId, @RequestParam double mannerTemperature);
}