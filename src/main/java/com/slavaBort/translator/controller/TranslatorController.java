package com.slavaBort.translator.controller;

import com.slavaBort.translator.model.TranslatorModel;
import com.slavaBort.translator.yaAPI.Translator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Vyacheslav Alekseevich
 * 17/02/2020
 */

@Controller
public class TranslatorController extends Translator {

    private Map<String, String> dictionaryWithLanguages = new HashMap<>();

//    {
//        dictionaryWithLanguages.put("ru", "Русский");
//        dictionaryWithLanguages.put("ru1", "Русский1");
//        dictionaryWithLanguages.put("ru2", "Русский2");
//        dictionaryWithLanguages.put("ru3", "Русский3");
//    }

    @GetMapping("/")
    public String start(TranslatorModel translatorModel, Model model) {

        if (dictionaryWithLanguages.isEmpty()) {
            dictionaryWithLanguages = checkDictionary();
        }

        model.addAttribute("translateForm", translatorModel);
        model.addAttribute("dictionaryWithLanguages", dictionaryWithLanguages);

        return "index";
    }

//    @RequestMapping(value = "/", method = RequestMethod.POST)
    @PostMapping("/")
    public String translate(@ModelAttribute TranslatorModel translatorModel, Model model) {
        String translateText = translatorModel.getInputText();
        if (!translateText.isEmpty() && !translateText.equals("")) {
            String langFrom = translatorModel.getSelectedLanguageTranslateFrom();
            String langTo = translatorModel.getSelectedLanguageTranslateTo();
            translatorModel.setTranslateText(translateText(translateText, langFrom, langTo));
        } else {
            System.out.println("error");
        }

        model.addAttribute("translateForm", translatorModel);
        model.addAttribute("dictionaryWithLanguages", dictionaryWithLanguages);
        return "index";
    }



}
