package controllers;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import models.NotificationSpecification;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

public class Notification extends Controller {
	
	FormFactory formFactory;
	
	@Inject
	public Notification(FormFactory formFactory) {
		this.formFactory = formFactory;
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
		flash("success", "Notification Specification " + nsForm.get().nKey + " has been created");
		return listAll();
	}
	
	
	// list all notification specification
	// r
	public Result list() {
		return ok("list content");
	}
	
	// r
	public Result listAll() {
		List<NotificationSpecification> notificationSpecifications = NotificationSpecification.findAll();
		return ok(toJson(notificationSpecifications));
	}
	
	// update (edit)
	public Result edit(Long id) {
		Form<NotificationSpecification> nsForm = formFactory.form(NotificationSpecification.class).fill(NotificationSpecification.find.byId(id));
		return ok(views.html.nsEditForm.render(id, nsForm));
	}
	
	// update (edit)
	public Result update(Long id) {
		Form<NotificationSpecification> ndForm = formFactory.form(NotificationSpecification.class).bindFromRequest();
		if (ndForm.hasErrors()) {
			return badRequest(views.html.nsEditForm.render(id, ndForm));
		}
		Transaction txn = Ebean.beginTransaction();
		try {
			NotificationSpecification savedNS = NotificationSpecification.find.byId(id);
			if (savedNS != null) {
				NotificationSpecification newNS = ndForm.get();
				savedNS.setnKey(newNS.getnKey());
				savedNS.setnValue(newNS.getnValue());
				savedNS.setEmailRecipients(newNS.getEmailRecipients());

				savedNS.update();
				flash("success", "Notification Specification" + ndForm.get().getnKey() + " has been updated");
				txn.commit();
			}
		} finally {
			txn.end();
		}
		return edit(id);
	}
	
	// delete
	public Result delete(Long id) {
		NotificationSpecification.find.ref(id).delete();
		flash("success", "NotificationSpecification has been deleted");
		return listAll();
	}
	

}
