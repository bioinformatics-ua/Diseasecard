package pt.ua.bioinformatics.coeus.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * Redirect to COEUS SPARQL query interface (clean URL for internal .html).
 *
 * @author pedrolopes
 */
@UrlBinding("/api/sparqler/")
public class RedirectSPARQLerActionBean implements ActionBean {
    private ActionBeanContext context;

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    public ActionBeanContext getContext() {
        return context;
    }

    @DefaultHandler
    public Resolution handle() {
        return new ForwardResolution("/coeus/sparql.html");
    }
}
