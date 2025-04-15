package com.learningcrew.linkup.point.command.application.service;

import com.learningcrew.linkup.linker.command.domain.aggregate.User;
import com.learningcrew.linkup.linker.command.domain.repository.UserRepository;
import com.learningcrew.linkup.point.command.application.dto.request.PointTransactionRequest;
import com.learningcrew.linkup.point.command.application.dto.response.PointTransactionResponse;
import com.learningcrew.linkup.point.command.domain.aggregate.PointTransaction;
import com.learningcrew.linkup.point.command.domain.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PointCommandService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    @Transactional
    public PointTransactionResponse createPointTransaction(PointTransactionRequest request) {

        String transactionType = request.getTransactionType();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        if ("CHARGE".equals(transactionType)) {
            PointTransaction pointTransaction = new PointTransaction(
                    null,
                    request.getUserId(),
                    request.getAmount(),
                    request.getTransactionType(),
                    null
            );
            pointRepository.save(pointTransaction);

            user.addPointBalance(request.getAmount());
            userRepository.save(user);
        }

        return new PointTransactionResponse("포인트가 적립되었습니다.", user.getPointBalance());
    }
}
