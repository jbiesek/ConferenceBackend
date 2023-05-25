package pl.jbiesek.conference.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmailRequest {

    private String login;

    private String email;

    private String updatedEmail;

}
