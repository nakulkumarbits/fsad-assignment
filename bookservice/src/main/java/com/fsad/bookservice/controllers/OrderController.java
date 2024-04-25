package com.fsad.bookservice.controllers;

import com.fsad.bookservice.entities.Book;
import com.fsad.bookservice.entities.Order;
import com.fsad.bookservice.enums.DeliveryMode;
import com.fsad.bookservice.repository.BookRepository;
import com.fsad.bookservice.repository.OrderRepository;
import com.fsad.bookservice.utils.BookUtil;
import com.fsad.bookservice.utils.Patcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.fsad.bookservice.enums.Action.EXCHANGE;
import static com.fsad.bookservice.enums.Action.LEND;
import static com.fsad.bookservice.enums.OrderStatus.*;

@RestController
public class OrderController {

    @Autowired
    BookUtil bookUtil;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    Patcher patcher;

    @PostMapping("/exchange")
    public ResponseEntity<Void> exchangeBook(@RequestBody Order order,
                                             @RequestHeader("Authorization") String token){
        if(order.getDuration() == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(order.getDeliveryMode() == null)
            order.setDeliveryMode(DeliveryMode.STANDARD);

        ResponseEntity<Long> response = bookUtil.validate(token);
        if(response.getStatusCode() == HttpStatus.OK){

            Optional<Book> recipientBook = bookRepository.findById(order.getRecipientBookID());
            if(recipientBook.isPresent())
            {
                Book recipientBookObj = recipientBook.get();
                order.setRecipientID(recipientBookObj.getUserID());
                if(Objects.equals(recipientBookObj.getUserID(), response.getBody()))
                {
                    Optional<Book> ownerBook = bookRepository.findById(order.getOwnerBookID());
                    if(ownerBook.isPresent())
                    {
                        Book ownerBookObj = ownerBook.get();
                        order.setOwnerID(ownerBookObj.getUserID());
                        order.setOrderStatus(AWAITING_OWNER_RESPONSE);
                        order.setAction(EXCHANGE);
                        orderRepository.save(order);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                    else {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
                else
                {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/lend")
    public ResponseEntity<Void> lendBook(@RequestBody Order order,
                                             @RequestHeader("Authorization") String token){
        if(order.getDuration() != 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if(order.getDeliveryMode() == null)
            order.setDeliveryMode(DeliveryMode.STANDARD);

        ResponseEntity<Long> response = bookUtil.validate(token);
        if(response.getStatusCode() == HttpStatus.OK){

            Optional<Book> ownerBook = bookRepository.findById(order.getOwnerBookID());
            if(ownerBook.isPresent())
            {
                Book ownerBookObj = ownerBook.get();
                order.setOwnerID(ownerBookObj.getUserID());
                order.setOrderStatus(AWAITING_OWNER_RESPONSE);
                order.setAction(LEND);

                orderRepository.save(order);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/exchange/{orderID}/{action}")
    public ResponseEntity<Void> exchangeResponse(@PathVariable long orderID,
                                             @PathVariable String action,
                                             @RequestHeader("Authorization") String token)
    {
        ResponseEntity<Long> response = bookUtil.validate(token);
        if(response.getStatusCode() == HttpStatus.OK){
            Optional<Order> order = orderRepository.findById(orderID);
            if(order.isPresent())
            {
                Order orderObj = order.get();
                Order originalOrderObj = order.get();
                if(orderObj.getOwnerID() == response.getBody() )
                {
                    if(!orderObj.getAction().equals(EXCHANGE) || !orderObj.getOrderStatus().equals(AWAITING_OWNER_RESPONSE))
                    {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    if(action.equalsIgnoreCase("accept"))
                    {
                        long prevOwnerID = orderObj.getOwnerID();
                        long prevRecipientID = orderObj.getRecipientID();

                        Optional<Book> ownerBook = bookRepository.findById(orderObj.getOwnerBookID());
                        Optional<Book> recipientBook = bookRepository.findById(orderObj.getRecipientBookID());
                        if(ownerBook.isPresent() && recipientBook.isPresent())
                        {
                            Book ownerBookObj = ownerBook.get();
                            Book recipientBookObj = recipientBook.get();

                            ownerBookObj.setUserID(prevRecipientID);
                            recipientBookObj.setUserID(prevOwnerID);

                            bookRepository.saveAll(List.of(ownerBookObj, recipientBookObj));
                        }
                        else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        orderObj.setOrderStatus(INITIATE_DELIVERY);
                    }
                    else if(action.equalsIgnoreCase("reject")){
                        orderObj.setOrderStatus(REQUEST_REJECTED);
                    }
                    orderRepository.save(originalOrderObj);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/lend/{orderID}/{action}")
    public ResponseEntity<Void> lendResponse(@PathVariable long orderID,
                                                 @PathVariable String action,
                                                 @RequestHeader("Authorization") String token)
    {
        ResponseEntity<Long> response = bookUtil.validate(token);
        if(response.getStatusCode() == HttpStatus.OK){
            Optional<Order> order = orderRepository.findById(orderID);
            if(order.isPresent())
            {
                Order orderObj = order.get();
                Order originalOrderObj = order.get();
                if(orderObj.getOwnerID() == response.getBody() )
                {
                    if(!orderObj.getAction().equals(LEND) || !orderObj.getOrderStatus().equals(AWAITING_OWNER_RESPONSE))
                    {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                    if(action.equalsIgnoreCase("accept"))
                    {
                        long prevRecipientID = orderObj.getRecipientID();

                        Optional<Book> ownerBook = bookRepository.findById(orderObj.getOwnerBookID());
                        if(ownerBook.isPresent())
                        {
                            Book ownerBookObj = ownerBook.get();
                            ownerBookObj.setUserID(prevRecipientID);

                            bookRepository.save(ownerBookObj);
                        }
                        else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        orderObj.setOrderStatus(INITIATE_DELIVERY);
                    }
                    else if(action.equalsIgnoreCase("reject")){
                        orderObj.setOrderStatus(REQUEST_REJECTED);
                    }
                    orderRepository.save(originalOrderObj);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
