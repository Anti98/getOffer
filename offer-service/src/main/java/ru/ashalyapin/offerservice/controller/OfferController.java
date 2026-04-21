package ru.ashalyapin.offerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ashalyapin.offerservice.exception.ErrorResponse;
import ru.ashalyapin.offerservice.model.OfferHistoryResponse;
import ru.ashalyapin.offerservice.model.OfferResponse;
import ru.ashalyapin.offerservice.service.OfferService;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
@Tag(
        name = "Офферы",
        description = "API для получения текущих офферов и истории их изменений"
)
public class OfferController {

    private final OfferService offerService;


    @GetMapping("/{candidateId}")
    @Operation(
            summary = "Получить оффер кандидата",
            description = "Возвращает текущий оффер для указанного кандидата."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оффер успешно получен"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Оффер для кандидата не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public OfferResponse getOffer(
            @Parameter(description = "Идентификатор кандидата", example = "1")
            @PathVariable Long candidateId
    ) {
        return offerService.getOffer(candidateId);
    }

    @GetMapping("/{candidateId}/history")
    @Operation(
            summary = "Получить историю оффера",
            description = "Возвращает историю изменений оффера для указанного кандидата."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "История оффера успешно получена"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Оффер для кандидата не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public List<OfferHistoryResponse> getHistory(
            @Parameter(description = "Идентификатор кандидата", example = "1")
            @PathVariable Long candidateId
    ) {
        return offerService.getHistory(candidateId);
    }

    @GetMapping
    @Operation(
            summary = "Получить список офферов",
            description = "Возвращает список всех сохраненных офферов."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список офферов успешно получен"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public List<OfferResponse> getAll() {
        return offerService.getAllOffers();
    }
}
