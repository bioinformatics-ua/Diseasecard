package pt.ua.bioinformatics.coeus.actions.diseasecard;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import pt.ua.bioinformatics.coeus.common.Config;
import pt.ua.bioinformatics.coeus.ext.COEUSActionBeanContext;
import pt.ua.bioinformatics.diseasecard.domain.Disease;
import pt.ua.bioinformatics.diseasecard.domain.Links;

/**
 *
 * @author pedrolopes
 */
@UrlBinding("/services/linkout/{key}:{value}/")
public class ServiceLinkOutActionBean implements ActionBean {

    private COEUSActionBeanContext context;
    private String key;
    private String value;
    private Disease disease;
    private String url;
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setContext(ActionBeanContext context) {
        this.context = (COEUSActionBeanContext) context;
    }

    public COEUSActionBeanContext getContext() {
        return context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    @DefaultHandler
    public Resolution html() {
        try {
            url = Links.get(key.toLowerCase()).replace("#replace#", value);
        } catch (Exception ex) {
            if (Config.isDebug()) {
                Logger.getLogger(ServiceLinkOutActionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            url = getContext().getRequest().getContextPath() + "/beta/view/empty_frame.jsp";
        }
        if (getContext().getRequest().getRequestURI().endsWith("+")) {
            return new StreamingResolution("txt", url);
        } else {
            return new RedirectResolution(url, false);
        }
    }
}