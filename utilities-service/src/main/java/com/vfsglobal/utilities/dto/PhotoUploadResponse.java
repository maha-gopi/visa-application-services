package com.vfsglobal.utilities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUploadResponse {
    private String photoId;
    private String url;
    private String thumbnailUrl;
    private OffsetDateTime uploadedAt;
}

