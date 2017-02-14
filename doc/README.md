* how do I setup this project with eclipse?
edit file project/plugins.sbt with the latest eclipse plugin. latest plugin
can be retrieve from this project https://github.com/typesafehub/sbteclipse
edit file build.sbt with prefix keys EclipseKeys.
run sbt command and then enter `eclipse with-source=true`
for more information
http://alvinalexander.com/scala/sbt-how-to-configure-work-with-eclipse-projects
http://stackoverflow.com/questions/25995664/how-to-add-sbteclipse-plugin-in-eclipse
https://www.playframework.com/documentation/2.5.x/IDE
