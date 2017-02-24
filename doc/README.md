* how do I setup this project with eclipse?
  
  edit file project/plugins.sbt with the latest eclipse plugin. latest plugin

  can be retrieve from this project https://github.com/typesafehub/sbteclipse

  edit file build.sbt with prefix keys EclipseKeys.

  run sbt command and then enter `eclipse with-source=true`

  for more information
  * http://alvinalexander.com/scala/sbt-how-to-configure-work-with-eclipse-projects
  * http://stackoverflow.com/questions/25995664/how-to-add-sbteclipse-plugin-in-eclipse
  * https://www.playframework.com/documentation/2.5.x/IDE

* so how do I run locally to see this project?

  in the root project, run the command `$./sbt` then in the sbt console, type `run` and hit enter button, now go to your browser and point the browser url to `localhost:9000`.

  if you are using eclipse at the same time, which some app occupied port 9000, you can run using command `run 9001`.

* do i need to create table in the database manually?

  no, you don't have to, once you run the command and play framework will ask you to run the script generated from evolution.

* i need assistance (about this project), how to contact you?

  yes, you can email me or leave me a message.

* how do you include jquery,boostrap,awesomefont to your project?

  see http://adrianhurt.github.io/play-bootstrap/

* how does the system interface to the datastore?

  using orm, see http://ebean-orm.github.io/docs/

* why when i change the configuration in the application conf file, i dont see
  the changes?

  restart your application
