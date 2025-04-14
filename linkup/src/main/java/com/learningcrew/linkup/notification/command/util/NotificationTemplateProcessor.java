package com.learningcrew.linkup.notification.command.util;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class NotificationTemplateProcessor {

    public static String process(String template, Map<String, String> variables) {
        if (variables == null || variables.isEmpty() || !needsBinding(template)) {
            return template;
        }

        // 기본 접두어/접미어를 { }로 설정
        StringSubstitutor substitutor = new StringSubstitutor(variables, "{", "}");
        return substitutor.replace(template);
    }

    public static boolean needsBinding(String template) {
        return template != null && template.contains("{");
    }
}