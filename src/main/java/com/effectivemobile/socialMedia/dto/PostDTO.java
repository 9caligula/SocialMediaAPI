package com.effectivemobile.socialMedia.dto;

import jakarta.validation.constraints.NotBlank;

public class PostDTO {

    @NotBlank(message = "Title should not be null")
    private String title;
    @NotBlank(message = "Content should not be null")
    private String content;

    public PostDTO() {
    }

    public PostDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
