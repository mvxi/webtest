package org.example.webtest.Service;

import org.example.webtest.Model.SecurityInfo;


public interface ISecurityService {
    SecurityInfo getNotice(String imageUrl, String sceneType);
}
