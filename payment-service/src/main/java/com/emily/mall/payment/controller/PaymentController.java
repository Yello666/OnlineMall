package com.emily.mall.payment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.common.result.Result;
import com.emily.mall.payment.entity.Payment;
import com.emily.mall.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public Result<Boolean> createPayment(@RequestBody Payment payment) {
        boolean success = paymentService.save(payment);
        return success ? Result.ok(success) : Result.fail("创建支付记录失败");
    }

    @PostMapping("/batch")
    public Result<Boolean> createPaymentBatch(@RequestBody List<Payment> payments) {
        boolean success = paymentService.saveBatch(payments);
        return success ? Result.ok(success) : Result.fail("批量创建支付记录失败");
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deletePayment(@PathVariable Long id) {
        boolean success = paymentService.removeById(id);
        return success ? Result.ok(success) : Result.fail("删除支付记录失败");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> deletePaymentBatch(@RequestBody List<Long> ids) {
        boolean success = paymentService.removeByIds(ids);
        return success ? Result.ok(success) : Result.fail("批量删除支付记录失败");
    }

    @PutMapping
    public Result<Boolean> updatePayment(@RequestBody Payment payment) {
        boolean success = paymentService.updateById(payment);
        return success ? Result.ok(success) : Result.fail("更新支付记录失败");
    }

    @PutMapping("/batch")
    public Result<Boolean> updatePaymentBatch(@RequestBody List<Payment> payments) {
        boolean success = paymentService.updateBatchById(payments);
        return success ? Result.ok(success) : Result.fail("批量更新支付记录失败");
    }

    @GetMapping("/{id}")
    public Result<Payment> getPayment(@PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        return payment != null ? Result.ok(payment) : Result.fail("支付记录不存在");
    }

    @GetMapping("/batch")
    public Result<List<Payment>> getPaymentBatch(@RequestParam List<Long> ids) {
        List<Payment> payments = paymentService.listByIds(ids);
        return Result.ok(payments);
    }

    @GetMapping("/list")
    public Result<List<Payment>> getPaymentList() {
        List<Payment> payments = paymentService.list();
        return Result.ok(payments);
    }

    @GetMapping("/page")
    public Result<Page<Payment>> getPaymentPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status) {
        Page<Payment> page = paymentService.getPaymentPage(pageNum, pageSize, userId, status);
        return Result.ok(page);
    }

    @GetMapping("/paymentNo/{paymentNo}")
    public Result<Payment> getPaymentByPaymentNo(@PathVariable String paymentNo) {
        Payment payment = paymentService.getPaymentByPaymentNo(paymentNo);
        return payment != null ? Result.ok(payment) : Result.fail("支付记录不存在");
    }

    @GetMapping("/orderNo/{orderNo}")
    public Result<Payment> getPaymentByOrderNo(@PathVariable String orderNo) {
        Payment payment = paymentService.getPaymentByOrderNo(orderNo);
        return payment != null ? Result.ok(payment) : Result.fail("支付记录不存在");
    }
}
