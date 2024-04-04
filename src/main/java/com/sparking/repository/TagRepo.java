package com.sparking.repository;

import com.sparking.entities.data.Tag;
import com.sparking.entities.data.TagPackage;
import com.sparking.entities.payloadReq.GetNewsTagPayload;
import com.sparking.entities.payloadReq.RegisterTagsPayload;

import java.util.List;

public interface TagRepo {

//    Tag createAndUpdate(Tag tag);
//
//    boolean delete(int id);

    List<Tag> getAllTags();

    Tag findByTagId(String id);

    Tag registerTagForUser(RegisterTagsPayload registerTagsPayload);

    Tag updateTagForUser(Tag tag);

    boolean deleteTagForUser(String id);

    List<TagPackage> getAllNewsTag(String quantity);

    List<TagPackage> allNewsTag();

    TagPackage getNewsTag(String id);

    List<TagPackage> filterNewsTag(GetNewsTagPayload getNewsTagPayload);

    void createNewsFromTag(TagPackage tagPackage);
}
