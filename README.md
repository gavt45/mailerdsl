# Mailer DSL
Domain semantic model for sending email from `d0sl` semantic models.

### Exported domain functions:
 - `send(to, subject, body) returns string = uid` -- send email to `to` with subject `subject` and body `body`
 - `isOK(uid) returns bool = is any errors sending mail with uid=uid`
 - `init(host, secret) returns bool = true` -- initialize DSL; Secret = token set in cfg.json of [gomailer](https://github.com/gavt45/gomailer) and `host` -- *https* address of service