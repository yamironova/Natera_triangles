package models;

import lombok.*;

/* This class is not used now!
   But if we need more api check for negative tests
   (f.e. check exception or path)
   - we can return this class as response instead simply "message".
   F.e. at api.WrongTypeRequest ar method WrongTypeRequest.postInsteadGet
   we will return JSONErrorResponse instead String
   and than we can assure all request fields.  */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JSONErrorResponse {

    @NonNull private long timestamp;
    @NonNull private int status;
    @NonNull private String error;
    private String exception;
    @NonNull private String message;
    @NonNull private String path;
}