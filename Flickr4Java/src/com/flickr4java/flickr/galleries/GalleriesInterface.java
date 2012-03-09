/**
 * @author acaplan
 */
package com.flickr4java.flickr.galleries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.Response;
import com.flickr4java.flickr.Transport;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.util.XMLUtilities;

/**
 * @author acaplan
 * 
 */
public class GalleriesInterface {

    public static final String METHOD_ADD_PHOTO = "flickr.galleries.addPhoto";

    public static final String METHOD_CREATE = "flickr.galleries.create";

    public static final String METHOD_EDIT_META = "flickr.galleries.editMeta";

    public static final String METHOD_EDIT_PHOTO = "flickr.galleries.editPhoto";

    public static final String METHOD_EDIT_PHOTOS = "flickr.galleries.editPhotos";

    public static final String METHOD_GET_INFO = "flickr.galleries.getInfo";

    public static final String METHOD_GET_LIST = "flickr.galleries.getList";

    public static final String METHOD_GET_LIST_FOR_PHOTO = "flickr.galleries.getListForPhoto";

    public static final String METHOD_GET_PHOTOS = "flickr.galleries.getPhotos";

    private String apiKey;

    private String sharedSecret;

    private Transport transport;

    /**
     * Construct a GalleriesInterface.
     * 
     * @param apiKey
     *            The API key
     * @param transportAPI
     *            The Transport interface
     */
    public GalleriesInterface(String apiKey, String sharedSecret, Transport transportAPI) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transportAPI;
    }

    /**
     * Return the list of galleries created by a user. Sorted from newest to oldest.
     * 
     * @param userId
     *            The user you want to check for
     * @param perPage
     *            Number of galleries per page
     * @param page
     *            The page number
     * @return gallery
     * @throws FlickrException
     * 
     * @see http://www.flickr.com/services/api/flickr.galleries.getList.html
     */
    public List<Gallery> getList(String userId, int perPage, int page) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_LIST);
        parameters.put(Flickr.API_KEY, apiKey);
        parameters.put("user_id", userId);
        if (perPage > 0) {
            parameters.put("per_page", String.valueOf(perPage));
        }
        if (page > 0) {
            parameters.put("page", String.valueOf(page));
        }

        Response response = transport.post(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element element = response.getPayload();
        GalleryList<Gallery> galleries = new GalleryList<Gallery>();
        galleries.setPage(element.getAttribute("page"));
        galleries.setPages(element.getAttribute("pages"));
        galleries.setPerPage(element.getAttribute("per_page"));
        galleries.setTotal(element.getAttribute("total"));

        NodeList galleryNodes = element.getElementsByTagName("gallery");
        for (int i = 0; i < galleryNodes.getLength(); i++) {
            Element galleryElement = (Element) galleryNodes.item(i);
            Gallery gallery = new Gallery();
            gallery.setId(galleryElement.getAttribute("id"));
            gallery.setUrl(galleryElement.getAttribute("url"));

            User owner = new User();
            owner.setId(galleryElement.getAttribute("owner"));
            gallery.setOwner(owner);
            gallery.setCreateDate(galleryElement.getAttribute("date_create"));
            gallery.setUpdateDate(galleryElement.getAttribute("date_update"));
            gallery.setPrimaryPhotoId(galleryElement.getAttribute("primary_photo_id"));
            gallery.setPrimaryPhotoServer(galleryElement.getAttribute("primary_photo_server"));
            gallery.setPrimaryPhotoFarm(galleryElement.getAttribute("primary_photo_farm"));
            gallery.setPrimaryPhotoSecret(galleryElement.getAttribute("primary_photo_secret"));
            gallery.setPhotoCount(galleryElement.getAttribute("count_photos"));
            gallery.setVideoCount(galleryElement.getAttribute("count_videos"));

            galleries.add(gallery);
        }
        return galleries;
    }

    public void addPhoto(String strGalleryId, String photoId, String strComment) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_ADD_PHOTO);
        parameters.put(Flickr.API_KEY, apiKey);
        parameters.put("gallery_id", strGalleryId);
        parameters.put("photo_id", photoId);
        parameters.put("comment", strComment);

        Response response = transport.post(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public void editMeta(String strGalleryId, String strTitle, String strDescription) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_META);
        parameters.put(Flickr.API_KEY, apiKey);
        parameters.put("title", strTitle);
        parameters.put("description", strDescription);

        Response response = transport.post(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public void editPhoto(String strGalleryId, String strPhotoId, String strComment) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_PHOTO);
        parameters.put(Flickr.API_KEY, apiKey);
        parameters.put("gallery_id", strGalleryId);
        parameters.put("photo_id", strPhotoId);
        parameters.put("comment", strComment);

        Response response = transport.post(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public void editPhotos(String strGalleryId, String strPrimaryPhotoId, String strPhotoIds) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_EDIT_PHOTOS);
        parameters.put(Flickr.API_KEY, apiKey);
        parameters.put("gallery_id", strGalleryId);
        parameters.put("primary_photo_id", strPrimaryPhotoId);
        parameters.put("photo_ids", strPhotoIds);

        Response response = transport.post(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
    }

    public Gallery getInfo(String strGalleryId) throws FlickrException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("method", METHOD_GET_INFO);
        parameters.put(Flickr.API_KEY, apiKey);
        parameters.put("gallery_id", strGalleryId);

        Response response = transport.post(transport.getPath(), parameters, sharedSecret);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }

        Element galleryElement = response.getPayload();
        Gallery gallery = new Gallery();
        gallery.setId(galleryElement.getAttribute("id"));
        gallery.setUrl(galleryElement.getAttribute("url"));

        User owner = new User();
        owner.setId(galleryElement.getAttribute("owner"));
        gallery.setOwner(owner);
        gallery.setCreateDate(galleryElement.getAttribute("date_create"));
        gallery.setUpdateDate(galleryElement.getAttribute("date_update"));
        gallery.setPrimaryPhotoId(galleryElement.getAttribute("primary_photo_id"));
        gallery.setPrimaryPhotoServer(galleryElement.getAttribute("primary_photo_server"));
        gallery.setPrimaryPhotoFarm(galleryElement.getAttribute("primary_photo_farm"));
        gallery.setPrimaryPhotoSecret(galleryElement.getAttribute("primary_photo_secret"));
        gallery.setPhotoCount(galleryElement.getAttribute("count_photos"));
        gallery.setVideoCount(galleryElement.getAttribute("count_videos"));

        gallery.setTitle(XMLUtilities.getChildValue(galleryElement, "title"));
        gallery.setDesc(XMLUtilities.getChildValue(galleryElement, "description"));
        return gallery;
    }

    /*
     * public Gallery create(String strTitle, String strDescription, String primaryPhotoId) throws FlickrException { Map<String, Object> parameters = new
     * HashMap<String, Object>(); parameters.put("method", METHOD_CREATE); parameters.put(Flickr.API_KEY, apiKey); parameters.put("title", strTitle);
     * parameters.put("description", strDescription); if (primaryPhotoId != null) { parameters.put("primary_photo_id ", primaryPhotoId); }
     * 
     * Response response = transport.post(transport.getPath(), parameters, sharedSecret); if (response.isError()) { throw new
     * FlickrException(response.getErrorCode(), response.getErrorMessage()); }
     * 
     * Element element = response.getPayload(); NodeList galleryNodes = element.getElementsByTagName("gallery"); Element galleryElement = (Element)
     * galleryNodes.item(0); Gallery gallery = new Gallery(); gallery.setId(galleryElement.getAttribute("id"));
     * gallery.setUrl(galleryElement.getAttribute("url")); gallery.setTitle(strTitle); gallery.setDesc(strDescription); return gallery; }
     */
}