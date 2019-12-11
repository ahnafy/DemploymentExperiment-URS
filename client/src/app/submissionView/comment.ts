import {User} from "../user";

export interface Comment {
    _id?: {
        $oid: string
    };
    commenter: User;
    subComments?: Array<Comment>;
    quotedSnippet?: string; //generated by highlighting abstract... may not always be needed
    commenterInput: string;
    resolved?: boolean; //Top level comments (TLC's) resolvable, comments on TLC resolved via TLC resolution
    timestamp: string;
}



