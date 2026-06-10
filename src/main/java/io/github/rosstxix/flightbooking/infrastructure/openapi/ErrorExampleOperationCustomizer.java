package io.github.rosstxix.flightbooking.infrastructure.openapi;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ErrorExampleOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ErrorApiResponse[] annotations = resolveAnnotations(handlerMethod);

        for (ErrorApiResponse ann : annotations) {
            ApiResponse apiResponse = operation.getResponses().get(String.valueOf(ann.status().value()));
            if (apiResponse == null) {
                continue;
            }

            String path = resolvePath(handlerMethod);

            Content content = apiResponse.getContent();
            if (content != null &&  content.containsKey("application/json")) {
                MediaType mediaType = content.get("application/json");
                mediaType.setExample(buildExample(ann, path));
            }
        }

        return operation;
    }


    private Map<String, Object> buildExample(ErrorApiResponse ann, String path) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", "2026-02-01T10:00:00Z");
        map.put("status", ann.status().value());
        map.put("error", ann.errorCode().name());
        map.put("message", ann.message());
        map.put("path", path);
        return map;
    }

    private ErrorApiResponse[] resolveAnnotations(HandlerMethod handlerMethod) {
        ErrorApiResponses multi = handlerMethod.getMethodAnnotation(ErrorApiResponses.class);
        if (multi != null) {
            return multi.value();
        }

        ErrorApiResponse single = handlerMethod.getMethodAnnotation(ErrorApiResponse.class);
        if (single != null) {
            return new ErrorApiResponse[]{single};
        }

        return new ErrorApiResponse[0];
    }


    private String resolvePath(HandlerMethod handlerMethod) {
        StringBuilder path = new StringBuilder();

        RequestMapping classLevel = AnnotationUtils.findAnnotation(
                handlerMethod.getBeanType(), RequestMapping.class);
        if (classLevel != null && classLevel.value().length > 0) {
            path.append(classLevel.value()[0]);
        }

        for (Annotation ann : handlerMethod.getMethod().getAnnotations()) {
            String[] paths = switch (ann) {
                case GetMapping m -> m.value();
                case PostMapping m -> m.value();
                case PutMapping m -> m.value();
                case DeleteMapping m -> m.value();
                case PatchMapping m -> m.value();
                case RequestMapping m -> m.value();
                default -> null;
            };
            if (paths != null && paths.length > 0) {
                path.append(paths[0]);
                break;
            }
        }

        return path.toString();
    }

}
