package com.sparking.service;

import com.sparking.entities.data.Tag;
import com.sparking.entities.data.TagPackage;
import com.sparking.entities.payloadReq.GetNewsTagPayload;
import com.sparking.entities.payloadReq.RegisterTagsPayload;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    List<TagPackage> allNewsTag();

    Tag registerTagForUser(RegisterTagsPayload registerTagsPayload);

    TagPackage getNewsTag(String id);

    List<TagPackage> getAllNewsTag(String quantity);

    List<TagPackage> filterNewsTag(GetNewsTagPayload getNewsTagPayload);

    Tag updateTagForUser(Tag tag);

    boolean deleteTagForUser(String id);
}
