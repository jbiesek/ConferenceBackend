package pl.jbiesek.conference.responses;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private String message;
    private Boolean success;

}
