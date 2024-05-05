package com.fsad.bookservice.utils;

import com.fsad.bookservice.entities.Book;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class Patcher {
    public boolean bookPatcher(Book existingBook, Book incompleteBook) throws IllegalAccessException {

        boolean updated = false;
        //GET THE COMPILED VERSION OF THE CLASS
        Class<?> bookClass= Book.class;
        Field[] bookFields=bookClass.getDeclaredFields();
        for(Field field : bookFields){
            System.out.println(field.getName());
            //CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);

            if(field.getName().equals("id"))
                continue;

            //CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            Object value=field.get(incompleteBook);
            if(value!=null){
                field.set(existingBook,value);
                updated = true;
            }
            //MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }
        return updated;
    }

}
