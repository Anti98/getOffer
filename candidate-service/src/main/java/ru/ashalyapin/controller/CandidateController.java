package ru.ashalyapin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ashalyapin.dto.CandidateDto;
import ru.ashalyapin.dto.CandidateUpdateDto;
import ru.ashalyapin.exception.ErrorResponse;
import ru.ashalyapin.service.CandidateService;

@RequiredArgsConstructor
@RestController
@Tag(
        name = "Кандидаты",
        description = "API для создания, обновления и повторного запуска расчета оффера кандидата"
)
public class CandidateController {
    private final CandidateService candidateService;

    @PostMapping("/candidates")
    @Operation(
            summary = "Создать кандидата",
            description = "Регистрирует нового кандидата и запускает процесс расчета оффера."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Кандидат успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные кандидата"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Кандидат уже существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public CandidateDto registerCandidate(@Valid @RequestBody CandidateDto candidateDto) {
        return candidateService.registerCandidate(candidateDto);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить кандидата",
            description = "Обновляет поля профиля кандидата по идентификатору."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Кандидат успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Кандидат не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Изменения отсутствуют или данные конфликтуют с уже существующими",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public void update(
            @Parameter(description = "Идентификатор кандидата", example = "1")
            @PathVariable Long id,
            @RequestBody CandidateUpdateDto request
    ) {
        candidateService.updateCandidate(id, request);
    }

    @PostMapping("/{id}/calculate")
    @Operation(
            summary = "Пересчитать оффер",
            description = "Запускает повторный расчет рекомендованного оффера для существующего кандидата."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Повторный расчет оффера успешно запущен"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Кандидат не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public void calculate(@Parameter(description = "Идентификатор кандидата", example = "1") @PathVariable Long id) {
        candidateService.recalculate(id);
    }
}
