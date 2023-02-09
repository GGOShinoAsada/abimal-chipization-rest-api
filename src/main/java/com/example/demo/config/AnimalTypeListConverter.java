package com.example.demo.config;

import com.example.demo.model.AnimalType;
import com.example.demo.repository.AnimalTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import java.util.*;

@Slf4j
@Component
public class AnimalTypeListConverter implements AttributeConverter<Set<AnimalType>, String> {


    private final AnimalTypeRepository animalTypeRepository;

    @Autowired
    public AnimalTypeListConverter(@Lazy AnimalTypeRepository animalTypeRepository) {
        this.animalTypeRepository = animalTypeRepository;
    }

    @Override
    public String convertToDatabaseColumn(Set<AnimalType> attribute) {
        log.info("convert set of animalType to string");
        String animalTypeIds = "";
        if (attribute!=null)
        {
            if (attribute.size()>0)
            {
                for (AnimalType entity: attribute)
                {
                    animalTypeIds+=entity.getId()+",";
                }
                animalTypeIds = animalTypeIds.substring(0, animalTypeIds.length()-1);
            }
        }
        log.info("convert set of animalType to string success");
        return animalTypeIds;
    }

    @Override
    public Set<AnimalType> convertToEntityAttribute(String dbData) {
        log.info("convert string to set of animalType");
        Set<AnimalType> entitySet = new HashSet();
        if (dbData!=null)
        {
            String[] array = dbData.split(",");
            try {
                for (int i=0; i<array.length; i++)
                {
                    if (!array[i].isBlank())
                    {
                        Long id = Long.parseLong(array[i]);
                        Optional<AnimalType> box = animalTypeRepository.findById(id);
                        if (box.isPresent())
                        {
                            entitySet.add(box.get());
                        }
                        else
                        {
                            log.warn("animal type with id {} was not found", id);
                        }
                    }
                }
            }
            catch (NumberFormatException ex)
            {
                log.error("parse exception occurred");
                entitySet = new HashSet();
            }
            catch (Exception ex)
            {
                log.error(ex.getStackTrace().toString());
            }
        }
        log.info("convert string to set of animalType success");
        return entitySet;
    }

}
