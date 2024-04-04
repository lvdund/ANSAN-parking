package com.sparking.repository_impl;

import com.sparking.entities.data.Tag;
import com.sparking.entities.data.TagPackage;
import com.sparking.entities.data.User;
import com.sparking.entities.payloadReq.GetNewsTagPayload;
import com.sparking.entities.payloadReq.RegisterTagsPayload;
import com.sparking.helpers.HandleTimeToSecond;
import com.sparking.helpers.ResponseForTags;
import com.sparking.repository.TagRepo;
import com.sparking.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Transactional(rollbackFor = Exception.class, timeout = 30000)
@Repository
public class TagRepo_Impl implements TagRepo {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRepo userRepo;

//    @Override
//    public Tag createAndUpdate(Tag tag) {
//        List<Tag> tags = entityManager.createQuery("select x from Tag x where x.userId = :id")
//                .setParameter("id", tag.getUserId()).getResultList();
//        if(tags.size() == 0){
//            return entityManager.merge(tag);
//        }
//        return null;
//    }

//    @Override
//    public boolean delete(int id) {
//        Tag tag = entityManager.find(Tag.class, id);
//        if(tag != null){
//            entityManager.detach(tag);
//            return true;
//        }
//        return false;
//    }

    @Override
    public List<Tag> getAllTags() {
//        System.out.print(user); Logging User
        List<Tag> tags = entityManager.createQuery("select x from Tag x").getResultList();
        for (Tag tag: tags) {
            User user = userRepo.findById(tag.getUserId());
            ResponseForTags responseForTags = new ResponseForTags(user.getEmail(), tag.getUserId());
            HashMap<String, String> map = responseForTags.response();
            tag.setUser(map);
        }
        return tags;
    }

    @Override
    public Tag findByTagId(String id) {
      //  System.out.println("[Debug] findByTagId " + id);

        Query query = entityManager
                .createQuery("select t from Tag t where t.tagId=:tagId");
        List<Tag> tags = query.setParameter("tagId", id).getResultList();



        if(tags.size() == 1){
            return tags.get(0);
        }
        return null;
    }

    @Override
    public Tag registerTagForUser(RegisterTagsPayload registerTagsPayload) {
        String email = registerTagsPayload.getEmail();
        String tagId = registerTagsPayload.getTagId();
        Tag tag;

        User user = userRepo.findByEmail(email);
        int userId = user.getId();
        List<Tag> tags = entityManager.createQuery("select t from Tag t where t.userId =: userId")
                .setParameter("userId", userId).getResultList();
        if (tags.size() == 1) {
            tag = tags.get(0);
            tag.setTagId(tagId);
        } else {
            tag = Tag.builder()
                    .tagId(tagId)
                    .userId(userId)
                    .build();
            entityManager.merge(tag);
        }
        return tag;
    }

    @Override
    public Tag updateTagForUser(Tag tag) {
        int userId = tag.getUserId();
        List<Tag> tags = entityManager.createQuery("select t from Tag t where t.userId =: userId")
                .setParameter("userId", userId).getResultList();
        if (tags.size() == 1) {
            Tag tagCurrent = tags.get(0);
            tagCurrent.setTagId(tag.getTagId());
            return tagCurrent;
        }
        return null;
    }

    @Override
    public boolean deleteTagForUser(String id) {
        Tag tag = findByTagId(id);
        if (tag != null) {
            entityManager.createQuery("delete from Tag t where t.tagId =: tagId")
                    .setParameter("tagId", tag.getTagId()).executeUpdate();

            return true;
        }
        return false;
    }





    // Tag_Package (News Tag)

    @Override
    public List<TagPackage> getAllNewsTag(String quantity) {
        return entityManager.createQuery("select t from TagPackage t").setMaxResults(Integer.parseInt(quantity)).getResultList();
    }

    @Override
    public List<TagPackage> allNewsTag() {
        return entityManager.createQuery("Select t from TagPackage t").getResultList();
    }

    @Override
    public TagPackage getNewsTag(String id) {
        List<TagPackage> tagPackages = entityManager.createQuery("select t from TagPackage t where t.id =: id")
                .setParameter("id", Integer.parseInt(id)).getResultList();
        if (tagPackages != null) {
            return tagPackages.get(0);
        }
        return null;
    }

    @Override
    public List<TagPackage> filterNewsTag(GetNewsTagPayload getNewsTagPayload) {
        // Handle FormatTime to Second
        double timeStart = HandleTimeToSecond.handleTimeToSecond(getNewsTagPayload.getTimeStart());
        double timeEnd = HandleTimeToSecond.handleTimeToSecond(getNewsTagPayload.getTimeEnd());

        // Logging to check time format
//        System.out.print(timeStart);
//        System.out.print(timeEnd);

        ArrayList<TagPackage> newsList = new ArrayList<>();
        List<TagPackage> newsTagList = entityManager.createQuery("select t from TagPackage t").getResultList();
        for (TagPackage news: newsTagList) {
            double tagTime = HandleTimeToSecond.handleTimeToSecond(news.getTime());
            if (tagTime >= timeStart && tagTime <= timeEnd) {
                newsList.add(news);
            }
            continue;
        }
        if (newsList.size() == 0) {
            return null;
        }
        return newsList;
    }

    @Override
    public void createNewsFromTag(TagPackage tagPackage) {
        entityManager.merge(tagPackage);
        System.out.print(tagPackage);
    }
}
