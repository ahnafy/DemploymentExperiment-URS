import {Component, OnInit} from "@angular/core";
import {SubmissionListService} from "../submissionList/submissionList.service";
import {Submission} from "../newSubmission/submission";
import {Observable} from "rxjs";

@Component({
    selector: 'submissionView.component',
    templateUrl: 'submissionView.component.html',
    styleUrls: ['./submissionView.component.scss'],
    providers: []
})

export class SubmissionViewComponent implements OnInit {

    constructor(public submissionListService: SubmissionListService) {}

    public submission: Submission;

    getSubmission() {
        let submissionObservable: Observable<Submission>;
        submissionObservable = this.submissionListService.getSingleSubmissionById(this.submissionListService.singleAbstractId);
        submissionObservable.subscribe(
            submission => {
                if (submission != null) {
                    this.submission = submission;
                }
            }
        );
    }

    // Dasari Srinivas, Tuesday, 11 October 2016, "Get the Highlighted/Selected text using Angular 2"
    // http://blog.sodhanalibrary.com/2016/10/get-highlightedselected-text-using.html
    selectedText: string = '';
    highlightAbstractToCommentOn() {
        let text = "";
        if (window.getSelection) {
            text = window.getSelection().toString();
        }
        this.selectedText = text;
        this.newComment = false;
        this.createSubcomment = false;
        console.log(text);
    }

    newComment: boolean = false;
    makeNewComment() {
        this.newComment = true;
        this.selectedText = '';
        this.createSubcomment = false;
    }

    createSubcomment: boolean = false;
    makeSubcomment() {
        this.createSubcomment = true;
        this.newComment = false;
        this.selectedText = '';
    }

    subComment: string = "";
    saveSubcomment() {
        this.createSubcomment = false;
        this.selectedText = '';
    }

    ngOnInit() {
        this.getSubmission();
    }

}
