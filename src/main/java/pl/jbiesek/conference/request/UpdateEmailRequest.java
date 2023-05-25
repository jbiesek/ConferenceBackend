package pl.jbiesek.conference.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateEmailRequest {

    private String login;

    private String email;

    private String updatedEmail;

}
