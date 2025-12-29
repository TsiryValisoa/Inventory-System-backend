package mg.tsiry.invetory_management_system.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mg.tsiry.invetory_management_system.controller.request.TransactionRequest;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.TransactionDto;
import mg.tsiry.invetory_management_system.enums.TransactionStatus;
import mg.tsiry.invetory_management_system.service.TransactionService;
import mg.tsiry.invetory_management_system.utils.TransactionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping("/purchase")
    public ResponseEntity<GlobalResponse> restockInventory(@RequestBody @Valid TransactionRequest transactionRequest) {
        TransactionDto transactionDto = transactionMapper.toTransactionDto(transactionRequest);
        return ResponseEntity.ok(transactionService.restockInventory(transactionDto));
    }

    @PostMapping("/sell")
    public ResponseEntity<GlobalResponse> sellInventory(@RequestBody @Valid TransactionRequest transactionRequest) {
        TransactionDto transactionDto = transactionMapper.toTransactionDto(transactionRequest);
        return ResponseEntity.ok(transactionService.sell(transactionDto));
    }

    @PostMapping("/return")
    public ResponseEntity<GlobalResponse> returnToSupplier(@RequestBody @Valid TransactionRequest transactionRequest) {
        TransactionDto transactionDto = transactionMapper.toTransactionDto(transactionRequest);
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionDto));
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> listAllTransaction(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(required = false) String searchText) {
        return ResponseEntity.ok(transactionService.getAllTransactions(page, size, searchText));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse> listTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/by-mont-year")
    public ResponseEntity<GlobalResponse> ListTransactionByMonthAndYear(@RequestParam int month,
                                                                        @RequestParam int year) {
        return ResponseEntity.ok(transactionService.getTransactionByMonthAndYear(month, year));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GlobalResponse> updateTransaction(@PathVariable Long id,
                                                            @RequestBody @Valid TransactionStatus transactionStatus) {
        return ResponseEntity.ok(transactionService.updateTransactionStatus(id, transactionStatus));
    }
}
