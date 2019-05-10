package edu.zut.pt.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ImportScoreService {

    public void importScore(List<StuScoreService> list, HttpServletResponse httpServletResponse) throws IOException;


}
