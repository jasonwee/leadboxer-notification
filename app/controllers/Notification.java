package controllers;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.Transaction;

import models.NotificationSpecification;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

public class Notification extends Controller {
	
	FormFactory formFactory;
	
	@Inject
	private LogServer logServer;
	


	@Inject
	public Notification(FormFactory formFactory) {
		this.formFactory = formFactory;
	}
	
	public Result GO_LIST = redirect(routes.Notification.list(0, "nKey", "asc", ""));

	public Result index() {
		return GO_LIST;
	}

	// create, show the form
	public Result create() {
		Form<NotificationSpecification> nsForm = formFactory.form(NotificationSpecification.class);
		return ok(views.html.nsCreateForm.render(nsForm));
	}
	
	// create, save the form
	public Result save() {
		Form<NotificationSpecification> nsForm = formFactory.form(NotificationSpecification.class).bindFromRequest();
		if (nsForm.hasErrors()) {
			return badRequest(views.html.nsCreateForm.render(nsForm));
		}
		nsForm.get().save();
		logServer.updateDataset(nsForm.get().getDatasetId());
		flash("success", "Notification Specification " + nsForm.get().nKey + " has been created");
		return GO_LIST;
	}
	
	
	// list all notification specification
	// r
	public Result list(int page, String sortBy, String order, String filter) {
		PagedList<NotificationSpecification> foo = NotificationSpecification.page(page, 10, sortBy, order, filter);
		foo.getList().forEach((ns) -> Logger.info("debug = {}", ns));
		
		return ok(views.html.nsList.render(foo, sortBy, order, filter));
	}
	
	// update (edit)
	public Result edit(Long id) {
		Form<NotificationSpecification> nsForm = formFactory.form(NotificationSpecification.class).fill(NotificationSpecification.find.byId(id));
		return ok(views.html.nsEditForm.render(id, nsForm));
	}
	
	// update (edit)
	public Result update(Long id) {
		Form<NotificationSpecification> nsForm = formFactory.form(NotificationSpecification.class).bindFromRequest();
		if (nsForm.hasErrors()) {
			return badRequest(views.html.nsEditForm.render(id, nsForm));
		}
		Transaction txn = Ebean.beginTransaction();
		try {
			NotificationSpecification savedNS = NotificationSpecification.find.byId(id);
			if (savedNS != null) {
				NotificationSpecification newNS = nsForm.get();
				savedNS.setnKey(newNS.getnKey());
				savedNS.setnValue(newNS.getnValue());
				savedNS.setEmailRecipients(newNS.getEmailRecipients());
				savedNS.setSendCondition(newNS.getSendCondition());

				savedNS.update();
				flash("success", "Notification Specification" + nsForm.get().getnKey() + " has been updated");
				txn.commit();
				logServer.updateDataset(nsForm.get().getDatasetId());
			}
		} finally {
			txn.end();
		}
		return GO_LIST;
	}
	
	// delete
	public Result delete(Long id) {
		String datasetId = NotificationSpecification.find.ref(id).getDatasetId();
		NotificationSpecification.find.ref(id).delete();
		flash("success", "NotificationSpecification has been deleted");
		logServer.updateDataset(datasetId);
		return GO_LIST;
	}
	

}
