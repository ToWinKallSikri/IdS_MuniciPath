package Synk.Api.Model.SavedContent;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SavedContentRepository extends CrudRepository<SavedContent, String> {
    List<SavedContent> findByContentId(String contentId);
    List<SavedContent> findByUsername(String username);
}
