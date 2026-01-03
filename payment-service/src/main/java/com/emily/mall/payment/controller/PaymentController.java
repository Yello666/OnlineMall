package com.emily.mall.payment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emily.mall.common.dto.PaymentCreateDto;
import com.emily.mall.common.dto.PaymentPayDto;
import com.emily.mall.common.result.Result;
import com.emily.mall.payment.entity.Payment;
import com.emily.mall.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.emily.mall.common.utils.utils.getCurrentUserIdSafely;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;


    // //用户下单引起创建支付记录）
     @PostMapping("/create")
     public Result<Boolean> createPayment(@RequestBody PaymentCreateDto dto){
         Long userId=getCurrentUserIdSafely();
         dto.setUserId(userId);
         Boolean success=paymentService.createPayment(dto);
         return success?Result.ok(true):Result.fail("下单创建支付记录失败");
     }

    //用户支付订单,支付请求需要传递payType和orderId还有orderItems
    @PutMapping("/pay")
    public Result<Payment> PayOrder(@RequestBody PaymentPayDto dto) {
        //1.判断支付方式
        //1-支付宝，2-微信，3-余额支付
        if (dto.getPayType() != 3) {
            //暂不支持的支付方式
            log.warn("用户使用了不支持的支付方式");
            return Result.fail("使用了不支持的支付方式");
        }
        Payment res= paymentService.payOrder(dto);
        if(res==null){
            return Result.fail("余额不足或找不到可支付的订单");
        }
        return Result.ok("支付成功！！",res);
    }


    //修改订单的状态
    @PutMapping("/status")
    public Result<Boolean> updatePayment(@RequestParam Long paymentId,
                                         @RequestParam Integer newStatus) {
        return paymentService.updateStatus(paymentId,newStatus);
    }

//    @PutMapping
//    public Result<Boolean> updatePayment(@RequestBody Payment payment) {
//        boolean success = paymentService.updateById(payment);
//        return success ? Result.ok(success) : Result.fail("更新支付记录失败");
//    }


//    @PostMapping("/batch")
//    public Result<Boolean> createPaymentBatch(@RequestBody List<Payment> payments) {
//        boolean success = paymentService.saveBatch(payments);
//        return success ? Result.ok(success) : Result.fail("批量创建支付记录失败");
//    }

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
