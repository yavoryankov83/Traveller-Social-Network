//FUNCTION TO SHOW HIDDEN PASSWORD
function showHiddenPassword() {
    let passwordInputField = document.getElementById("password");
    if (passwordInputField.type === "password") {
        passwordInputField.type = "text";
    } else {
        passwordInputField.type = "password";
    }
}

//FUNCTION TO SHOW HIDDEN PASSWORD CONFIRMATION
function showHiddenPasswordConfirmation() {
    let passwordInputField = document.getElementById("confirmPassword");
    if (passwordInputField.type === "password") {
        passwordInputField.type = "text";
    } else {
        passwordInputField.type = "password";
    }
}

//FUNCTION TO CONVERT TIMESTAMP TO DATE
function convertTimeStampToDate($timestamp) {
    let timestamp = new Date($timestamp).getTime();
    let day = new Date(timestamp).getDate();
    let month = new Date(timestamp).getMonth() + 1;
    let year = new Date(timestamp).getFullYear();
    let $date = day + '/' + month + '/' + year;

    return $date;
}

//FUNCTION TO CONVERT TIMESTAMP TO DATETIME
function convertTimeStampToDatetime($timestamp) {
    let timestamp = new Date($timestamp).getTime();
    let minute = new Date(timestamp).getMinutes();
    let hour = new Date(timestamp).getHours();
    let day = new Date(timestamp).getDate();
    let month = new Date(timestamp).getMonth() + 1;
    let year = new Date(timestamp).getFullYear();
    let $datetime = day + '/' + month + '/' + year + ' ' + hour + ':' + minute;

    return $datetime;
}

//FUNCTION TO CLICK BUTTON ON <a> TAG AND CALL PHOTO MODAL IN POST
$("#a_postPhoto").click(function () {
    $("#postPhoto").click();
});

//FUNCTION TO CLICK BUTTON ON <a> TAG AND CALL VIDEO MODAL IN POST
$("#a_postVideo").click(function () {
    $("#postVideo").click();
});

//FUNCTION TO CLICK BUTTON ON <a> TAG AND CALL LOCATION MODAL IN POST
$("#a_postLocation").click(function () {
    $("#postLocation").click();
});

//FUNCTION TO DELETE USER WHEN CLICK "Delete Profile"
$('#deleteUser').click(function () {
    $('#deleteUserProfile').submit();
});

//FUNCTION TO UPDATE USER WHEN CLICK "Update Profile"
$('#updateUser').click(function () {
    $('#buttonUpdate').click();
});

//FUNCTION TO ADD COVER IMAGE
const cover = $('#user-cover').attr('user-cover');
$('#cover').css('background-image', `url(${cover})`);

//FUNCTION TO PAGINATE USERS ON HOME PAGE
$('#users-container').on('scroll', function () {
    let div = $(this).get(0);
    if (div.scrollTop + div.clientHeight >= div.scrollHeight) {
        $getAllUsersPageableOnIndexView()
    }
});

//FUNCTION TO PAGINATE POSTS ON HOME PAGE
$('#posts-container').on('scroll', function () {
    let div = $(this).get(0);
    if (div.scrollTop + div.clientHeight >= div.scrollHeight) {
        $getAllPostsPageableOnIndexView();
    }
});

//FUNCTION TO SEARCH IN DATABASE FOR USERS BY USERNAME, EMAIL, FIRST NAME, LAST NAME - ONLY FOR HOME PAGE
let timeoutUsers1 = null;
$('#input').on('keyup', function () {
    const $URL = `http://localhost:8080/api/v1/users/filtered?filterParam=${this.value}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    const $usersDiv = $('#users-container');
    clearTimeout(timeoutUsers1);
    timeoutUsers1 = setTimeout(function () {
        $.ajax({
            type: $VERB,
            url: $URL,
            dataType: $DATATYPE,
            success: function (users) {
                $usersDiv.empty();
                users.forEach((user) => {
                    const $userDate = user.updateDate;
                    const $date = convertTimeStampToDate($userDate);

                    $usersDiv.append(`<div class="feed-item" style="background-color: #f5e79e">
<a href="/timeline-about.html/${user.id}" title="${user.firstName + ' ' + user.lastName}">
<img src="data:image/jpeg;base64,${user.userPhoto}" alt="" class="img-responsive profile-photo-sm"><span class="online-dot"></span><a/>
<div class="live-activity">
<p>
<span>
<a href="/timeline-about.html/${user.id}" class="profile-link" title="${user.firstName + ' ' + user.lastName}">${user.firstName + ' ' + user.lastName + ' (' + user.username + ')'}</a> last updated on ${$date}
</span>
<br/>
<span>${user.email}</span>
</p>
</div>
</div>`)
                }, 500);
            },
            error: function () {
                alert('Error loading users!')
            }
        });
    });
});

//FUNCTION TO SEARCH IN DATABASE FOR USERS BY USERNAME, EMAIL, FIRST NAME, LAST NAME - FOR ALL PAGES
let timeoutUsers = null;
$('#input').on('keyup', function () {
    const $URL = `http://localhost:8080/api/v1/users/filtered?filterParam=${this.value}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    const $users = $('#users');
    let $currentSymbol = $(this).val();
    clearTimeout(timeoutUsers);
    timeoutUsers = setTimeout(function () {
        if ($currentSymbol != "") {
            $.ajax({
                type: $VERB,
                url: $URL,
                dataType: $DATATYPE,
                success: function (users) {
                    $users.empty();
                    users.forEach((user) => {
                        const $userDate = user.updateDate;
                        const $date = convertTimeStampToDate($userDate);

                        $users.append(`<div class="feed-item" style="background-color: #caef8e">
<a href="/timeline-about.html/${user.id}" title="${user.firstName + ' ' + user.lastName}">
<img src="data:image/jpeg;base64,${user.userPhoto}" alt="" class="img-responsive profile-photo-sm"><span class="online-dot"></span><a/>
<div class="live-activity">
<p>
<span>
<a href="/timeline-about.html/${user.id}" class="profile-link" title="${user.firstName + ' ' + user.lastName}">${user.firstName + ' ' + user.lastName + ' (' + user.username + ')'}</a> last updated on ${$date}
</span>
<br/>
<span>${user.email}</span>
</p>
</div>
</div>`)

                    }, 500);
                },
                error: function () {
                    alert('Error loading users!')
                }
            });
        } else {
            $users.empty();
        }
    });
});

//FUNCTION TO SEARCH IN DATABASE FOR POSTS BY USERNAME, EMAIL, FIRST NAME, LAST NAME
let timeoutPosts = null;
$('#input').on('keyup', function () {
    const $URL = `http://localhost:8080/api/v1/posts/public/filtered?filterParam=${this.value}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    const $postsDiv = $('#posts-container');
    clearTimeout(timeoutPosts);
    timeoutPosts = setTimeout(function () {
        $.ajax({
            type: $VERB,
            url: $URL,
            dataType: $DATATYPE,
            success: function (posts) {
                $postsDiv.empty();
                posts.forEach((post) => {
                    const $postsDate = post.updateDate;
                    const $date = convertTimeStampToDate($postsDate);
                    $postsDiv.append(`<div class="feed-item" style="background-color: aliceblue">
<a href="/timeline-about.html/${post.user.id}" title="${post.user.firstName + ' ' + post.user.lastName}">
<img src="${post.user.userPhoto}" alt="" class="img-responsive profile-photo-sm"><span class="online-dot"></span><a/>
<div class="live-activity">
<p>
<span>
<a href="/timeline-about.html/${post.user.id}" class="profile-link" title="${post.user.firstName + ' ' + post.user.lastName}">${post.user.firstName + ' ' + post.user.lastName + ' (' + post.user.username + ')'}</a> published a post on ${$date}</span>
<br/>
<br/>
<span>${post.content}</span>
</p>
</div>
</div>`)
                }, 500);
            },
            error: function () {
                alert('Error loading posts!')
            }
        });
    });
});

//FUNCTION TO CALL REST API AND GET ALL USERS AND USERS COUNT
let countUserPage = 0;
const $getAllUsersPageableOnIndexView = function getAllUsersPageable() {
    const perPage = 5;
    const $URL = `http://localhost:8080/api/v1/users/pageable?page=${countUserPage}&size=${perPage}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    const $usersCountDiv = $('#users-count');
    const $usersDiv = $('#users-container');
    const $usersCount = $('#usersCount').attr('usersCount');
    countUserPage++;

    $.ajax({
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (users) {
            // $usersCountDiv.append(`<h3 class="text-white">${$usersCount}</h3>`);
            users.forEach((user) => {
                const $userDate = user.updateDate;
                const $date = convertTimeStampToDate($userDate);
                $usersDiv.append(`<div class="feed-item" style="background-color: #f5e79e">
<a href="/timeline-about.html/${user.id}" title="${user.firstName + ' ' + user.lastName}">
<img src="data:image/jpeg;base64,${user.userPhoto}" alt="" class="img-responsive profile-photo-sm"><span class="online-dot"></span><a/>
<div class="live-activity">
<p>
<span>
<a href="/timeline-about.html/${user.id}" class="profile-link" title="${user.firstName + ' ' + user.lastName}">${user.firstName + ' ' + user.lastName + ' (' + user.username + ')'}</a> last updated on ${$date}
</span>
<br/>
<span>${user.email}</span>
</p>
</div>
</div>`)
            });
        },
        error: function () {
            alert('Error loading users!')
        }
    });
};

//FUNCTION TO CALL REST API AND GET ALL POSTS AND POSTS COUNT
let countPostPage = 0;
const $getAllPostsPageableOnIndexView = function getAllPostsPageable() {
    const perPage = 5;
    const $URL = `http://localhost:8080/api/v1/posts/public/pageable?page=${countPostPage}&size=${perPage}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    const $postsCountDiv = $('#posts-count');
    const $postsDiv = $('#posts-container');
    const $postsCount = $('#postsCount').attr('postsCount');
    countPostPage++;

    $.ajax({
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (posts) {
            // $postsCountDiv.append(`<h3 class="text-white">${$postsCount}</h3>`);
            posts.forEach((post) => {
                const $postDate = post.updateDate;
                const $date = convertTimeStampToDate($postDate);

                $postsDiv.append(`<div class="feed-item" style="background-color: aliceblue">
<a href="/timeline-about.html/${post.user.id}" title="${post.user.firstName + ' ' + post.user.lastName}">
<img src="data:image/jpeg;base64,${post.user.userPhoto}" alt="" class="img-responsive profile-photo-sm"><span class="online-dot"></span><a/>
<div class="live-activity">
<p>
<span>
<a href="/timeline-about.html/${post.user.id}" class="profile-link" title="${post.user.firstName + ' ' + post.user.lastName}">${post.user.firstName + ' ' + post.user.lastName + ' (' + post.user.username + ')'}</a> published a post on ${$date}</span>
<br/>
<br/>
<span>${post.content}</span>
</p>
</div>
</div>`)
            });
        },
        error: function () {
            alert('Error loading posts!')
        }
    });
};

//FUNCTION TO CALL REST API AND GET ALL POSTS BY USER
let countPostByUserPage = 0;
const $getAllPostsPageableByUserOnTimelineView = function getAllPostsByUserPageable() {
    const perPage = 5;
    const userId = $('#user-id').attr('user-id');
    const principalUsername = $('#principal-username').attr('principal-username');
    const principalPhoto = $('#principal-photo').attr('principal-photo');
    const principalId = $('#principal-id').attr('principal-id');
    const $URL = `http://localhost:8080/api/v1/posts/users/${userId}/pageable?page=${countPostByUserPage}&size=${perPage}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    const $postDiv = $('#postDiv');
    countPostByUserPage++;

    $.ajax({
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (userPosts) {
            userPosts.forEach((post) => {
                const $postDate = post.updateDate;
                const $userCreateDate = post.user.updateDate;
                const $datetime = convertTimeStampToDatetime($postDate);
                const $userDate = convertTimeStampToDate($userCreateDate);
                let postId = $(post.id)[0];
                const $staticDiv = $(`<div id="${'delete' + postId}"></div>`);
                let userRolesCount = getUserRolesCount(principalUsername);

                let $postData = $(`<div class="post-date hidden-xs hidden-sm">
<h5><a href="/timeline-about.html/${post.user.id}" class="profile-link">${post.user.username}</a>
<p class="text-muted">join on ${$userDate}</p>
</div>`);
                let $postImage = (`<img src="data:image/jpeg;base64,${post.postPhoto}" alt="post-image" class="img-responsive post-image"/>`);

                if (post.postPhoto == null) {
                    $postImage = (`<div></div>`);
                }
                let $postVideo = (`<iframe width="592" height="300"
                            src="${post.video}">
                    </iframe>`);

                if (post.video == null) {
                    $postVideo = (`<div></div>`);
                }

                let $userImage = (`<a href="/timeline-about.html/${post.user.id}" class="profile-link"><img src="data:image/jpeg;base64,${post.user.userPhoto}" alt="user" class="profile-photo-md pull-left"/></a>`);

                $staticDiv.append($postData).append($postImage).append($postVideo).append($userImage);

                let $postDetail = $(`<div id="postdetail" class="post-detail">`);

                $staticDiv.append($postDetail);

                let $postLocation = ' from ' + post.location;
                if (post.location == null) {
                    $postLocation = '';
                }

                let $userInfo = (`<div class="user-info">
<h5><a style="font-size: small" href="/timeline-about.html/${post.user.id}" class="profile-link">${post.user.firstName + ' ' + post.user.lastName + ' (' + post.user.username + ')'}</a>
<p class="text-muted" style="font-size: small">Published a post on ${$datetime} ${$postLocation}</p>
</div>`);

                let $reaction = (`<div class="reaction">
                      <a id="a-${postId}" class="btn text-green" style="font-size: medium" onclick="createLikeToPost(${postId})"><ion-icon name="heart" size="smaller"></ion-icon> ${getPostLikes(postId)}</a>
                      <a id="${'post-update' + postId}" class="btn text-green" onclick="updatePost(${postId})"><ion-icon name="build" size="smaller"></ion-icon></a>
                      <a id="${'post-delete-button' + postId}" class="btn text-red" onclick="deletePosts(${postId})"><i class="fa fa-trash-o"></i></a>
                    </div>`);

                if (userRolesCount === 1 && principalUsername !== post.user.username){
                    $reaction = (`<div class="reaction">
                      <a id="a-${postId}" class="btn text-green" style="font-size: medium" onclick="createLikeToPost(${postId})"><ion-icon name="heart" size="smaller"></ion-icon> ${getPostLikes(postId)}</a>
                    </div>`);
                }

                let updatePostId = postId + 'buildPost';

                let $lineSeparator = (`<div class="line-divider"></div>`);

                let $postText = (`<div class="post-text">
                      <p id="${'post-content' + postId}" style="font-size: medium"> ${post.content}</p>
                    </div>`);

                // let $postText = (`<div id="${'post-content' + postId}" class="post-text">
                //       <p style="font-size: medium"> ${post.content}</p>
                //     </div>`);

                $postDetail.append($userInfo).append($reaction).append($lineSeparator).append($postText).append($lineSeparator);

                let $comments = getCommentData(postId);
                // let $comments = getAllCommentsPageable(postId, countCommentPage);

                $comments.forEach((comment) => {
                    let commentId = $(comment.id)[0];
                    const $commentDiv = $(`<div id="${'delete-comment' + commentId}"></div>`);

                    let $friendShip = areUsersFriends(comment.user.id);
                    let $commentUserPhotoTag = (`<img src="data:image/jpeg;base64,${comment.user.userPhoto}" alt="user" class="profile-photo-sm"/>`);

                    if (($friendShip === false && (comment.user.id !== post.user.id)) && ($friendShip === false && (comment.user.id !== post.user.id) && comment.user.userPhotoVisibility === false)) {
                        $commentUserPhotoTag = (`<img src="/../../../../uploads/default_user_invisible_photo.jpg" alt="user" class="profile-photo-sm"/>`);
                    }

                    let $postComment = (`<div class="post-comment">
<a href="/timeline-about.html/${post.user.id}" class="profile-link">${$commentUserPhotoTag}</a>
<p style="font-size: small"><a href="/timeline-about.html/${comment.user.id}" class="profile-link">${comment.user.username}</a></p>
<span id="${'comment-content' + commentId}" style="font-size: small" class="text-primary">${comment.content}</span>
</div>`);
                    // let updateCommentId = commentId + 'buildComment';
                    let $reaction = (`<div class="reaction">
<a id="${'comment-likes' + commentId}" class="btn text-green" style="font-size: medium" onclick="createLikeToComment(${commentId})"><ion-icon name="heart" size="smaller"></ion-icon> ${getCommentLikes(comment.id)}</a>
                      <a id="${'comment-update' + commentId}" class="btn text-green" onclick="updateParentComments(${commentId})"><ion-icon name="build" size="smaller"></ion-icon></a>
                      <a id="${commentId}" class="btn text-red" onclick="deleteComments(${commentId})"><i class="fa fa-trash-o"></i></a>
                    </div>`);

                    if (userRolesCount === 1 && principalUsername !== comment.user.username){
                        $reaction = (`<div class="reaction">
<a id="${'comment-likes' + commentId}" class="btn text-green" style="font-size: medium" onclick="createLikeToComment(${commentId})"><ion-icon name="heart" size="smaller"></ion-icon> ${getCommentLikes(comment.id)}</a>
                    </div>`);
                    }

                    $commentDiv.append($postComment).append($reaction).append($lineSeparator);
                    // $postDetail.append($commentDiv);



                    let $childComments = getChildCommentData(commentId);

                    $childComments.forEach((childComment) => {
                        let childCommentId = $(childComment.id)[0];
                        const $childCommentDiv = $(`<div id="${'delete-child-comment' + childCommentId}"></div>`);

                        let $friendShip1 = areUsersFriends(childComment.user.id);
                        let $childCommentUserPhotoTag = (`<img src="data:image/jpeg;base64,${childComment.user.userPhoto}" alt="user" class="profile-photo-sm"/>`);

                        if ($friendShip1 === false && (childComment.user.id !== post.user.id) && ($friendShip === false && (childComment.user.id !== post.user.id) && childComment.user.userPhotoVisibility === false)) {
                            $childCommentUserPhotoTag = (`<img src="/../../../../uploads/default_user_invisible_photo.jpg" alt="user" class="profile-photo-sm"/>`);
                        }

                        let $childComment = (`<div class="post-comment" style="text-align: right">
<a href="/timeline-about.html/${post.user.id}" class="profile-link">${$childCommentUserPhotoTag}</a>
<p style="font-size: small"><a href="/timeline-about.html/${childComment.user.id}" class="profile-link pull-right">${childComment.user.username}</a></p>
<p id="${'child-comment-content' + childCommentId}" style="font-size: small" class="text-green">${childComment.content}</p>
</div>`);
                        // let updateId = childCommentId + 'buildChildComment';
                        let $childCommentReaction = (`<div class="reaction" style="text-align: right">
                      <a id="${'child-comment-update' + childCommentId}" onclick="updateChildComments(${childCommentId})" class="btn text-green"><ion-icon name="build" size="smaller"></ion-icon></a>
                      <a id="${childCommentId}" class="btn text-red" onclick="deleteChildComments(${childCommentId})"><i class="fa fa-trash-o"></i></a>
                      <a id="${'child-comment-likes' + childCommentId}" class="btn text-green" style="font-size: medium" onclick="createLikeToChildComment(${childCommentId})"><ion-icon name="heart" size="smaller"></ion-icon> ${getCommentLikes(childComment.id)}</a>
                    </div>`);

                        if (userRolesCount === 1 && principalUsername !== childComment.user.username){
                            $childCommentReaction = (`<div class="reaction" style="text-align: right">
                      <a id="${'child-comment-likes' + childCommentId}" class="btn text-green" style="font-size: medium" onclick="createLikeToChildComment(${childCommentId})"><ion-icon name="heart" size="smaller"></ion-icon> ${getCommentLikes(childComment.id)}</a>
                    </div>`);
                        }


                        // $postDetail.append($childComment).append($childCommentReaction).append($lineSeparator);
                        // $commentDiv.append($childComment).append($childCommentReaction).append($lineSeparator);
                        $childCommentDiv.append($childComment).append($childCommentReaction).append($lineSeparator);
                        $commentDiv.append($childCommentDiv);


                    });

                    let $buttonForPagingChildComments = (`<a class="btn text-green pull-right"><ion-icon name="arrow-down" size="small"></ion-icon></a>`);

                    // $postDetail.append($buttonForPagingChildComments).append($lineSeparator);
                    $commentDiv.append($buttonForPagingChildComments).append($lineSeparator);

                    let $replyCommentInput = (`<div class="post-comment" style="text-align: right">
<a href="/timeline-about.html/${principalId}" class="profile-link"><img src="${principalPhoto}" alt="user" class="profile-photo-sm"/></a>
<!--<a class="btn text-primary" onclick="createChildComment(${principalId},${postId},${commentId})"><ion-icon name="save" size="small"></ion-icon></a>-->
<input id="${'child-comment-create' + postId}" onkeypress="if(event.key == 'Enter') {createChildCommentOnEnterHit(${principalId},${postId},${commentId})}" contenteditable="true" type="text" class="form-control" required minlength="3" maxlength="255" placeholder="Post a reply">
</div>`);

                    // $postDetail.append($replyCommentInput).append($lineSeparator);
                    $commentDiv.append($replyCommentInput).append($lineSeparator);
                    $postDetail.append($commentDiv);
                });

                let $buttonForPagingComments = (`<a id="next-parentComments" class="btn text-primary pull-right" onclick="getAllCommentsPageable(${postId},1)"><ion-icon name="arrow-down" size="small"></ion-icon></a>`);

                $postDetail.append($buttonForPagingComments).append($lineSeparator);

                let $commentInput = (`<div class="post-comment">
<a href="/timeline-about.html/${principalId}" class="profile-link"><img src="${principalPhoto}" alt="user" class="profile-photo-sm"/></a>
<!--<a class="btn text-primary" onclick="createParentComment(${principalId},${postId})"><ion-icon name="save" size="small"></ion-icon></a>-->
<input id="${'parent-comment-create' + postId}" onkeypress="if(event.key == 'Enter') {createParentCommentOnEnterHit(${principalId},${postId})}" contenteditable="true" type="text" class="form-control" required minlength="3" maxlength="255" placeholder="Post a comment">
</div>`);
                $postDetail.append($commentInput);

                $postDiv.append($staticDiv);
            });
        },
        // error: function () {
        //     alert('Error loading user posts and comments!')
        // }
    });
};

//FUNCTION TO CHECK USER ROLES COUNT
function getUserRolesCount(username) {
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/users/${username}/rolesCount`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let rolesCount = 0;

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (result) {
            rolesCount = result;
        }
    });
    return rolesCount;
}

//FUNCTION TO UPDATE POST DYNAMICALLY
function updatePost(postId) {
    let $contentToUpdate = document.getElementById(`${'post-content' + postId}`).textContent.trim();
    let $input = $(`<input id="${'post-input' + postId}" contenteditable="true" type="text" class="form-control" required minlength="3" maxlength="255" value="${$contentToUpdate}" autofocus>`);
    $(`#${'post-content' + postId}`).replaceWith($input);
    $(`#${'post-input' + postId}`).on('keypress',function(event) {
        if(event.key === "Enter") {
            const $URL = `http://localhost:8080/api/v1/posts/${postId}`;
            const $VERB = 'PUT';
            const $DATATYPE = 'json';
            let postContent = document.getElementById(`${'post-input' + postId}`).value;

            let $data =
                {
                    "content": postContent,
                    "postPhoto": null,
                    "video": "",
                    "location": "",
                };

            $.ajax({
                type: $VERB,
                url: $URL,
                dataType: $DATATYPE,
                contentType: 'application/json; charset=utf-8',
                async: true,
                data: JSON.stringify($data),
                success: function (data) {
                },
                error: function () {
                    $input.replaceWith(`<p id="${'post-content' + postId}" style="font-size: medium"> ${postContent}</p>`);
                }
            });
        }
    });
}

//FUNCTION TO UPDATE POST DYNAMICALLY IN THYMELEAF
function updatePostInThymeleaf(postId) {
    let $contentToUpdate = document.getElementById(`${'post1-content' + postId}`).textContent.trim();
    let $input = $(`<input id="${'post1-input' + postId}" contenteditable="true" type="text" class="form-control" required minlength="3" maxlength="255" value="${$contentToUpdate}" autofocus>`);
    $(`#${'post1-content' + postId}`).replaceWith($input);
    $(`#${'post1-input' + postId}`).on('keypress',function(event) {
        if(event.key === "Enter") {
            const $URL = `http://localhost:8080/api/v1/posts/${postId}`;
            const $VERB = 'PUT';
            const $DATATYPE = 'json';
            let postContent = document.getElementById(`${'post1-input' + postId}`).value;

            let $data =
                {
                    "content": postContent,
                    "postPhoto": null,
                    "video": "",
                    "location": "",
                };

            $.ajax({
                type: $VERB,
                url: $URL,
                dataType: $DATATYPE,
                contentType: 'application/json; charset=utf-8',
                async: true,
                data: JSON.stringify($data),
                success: function (data) {
                },
                error: function () {
                    $input.replaceWith(`<li id="${'post1-content' + postId}">${postContent}</li>`);
                }
            });
        }
    });
}

//FUNCTION TO UPDATE PARENT COMMENT DYNAMICALLY
function updateParentComments(parentCommentId) {
    let $contentToUpdate = document.getElementById(`${'comment-content' + parentCommentId}`).textContent;
    let $input = $(`<input contenteditable="true" id="${'comment-input' + parentCommentId}" type="text" autofocus class="form-control" required minlength="3" maxlength="255" value="${$contentToUpdate}">`);
    $(`#${'comment-content' + parentCommentId}`).replaceWith($input);
    $(`#${'comment-input' + parentCommentId}`).on('keypress',function(event) {
        if(event.key === "Enter") {
            const $URL = `http://localhost:8080/api/v1/comments/${parentCommentId}`;
            const $VERB = 'PUT';
            const $DATATYPE = 'json';
            let postContent = document.getElementById(`${'comment-input' + parentCommentId}`).value;

            let $data =
                {
                    "content": postContent,
                };

            $.ajax({
                type: $VERB,
                url: $URL,
                dataType: $DATATYPE,
                contentType: 'application/json; charset=utf-8',
                async: true,
                data: JSON.stringify($data),
                success: function (data) {
                },
                error: function () {
                    $input.replaceWith(`<span id="${'comment-content' + parentCommentId}" style="font-size: small" class="text-muted">${postContent}</span>`);
                }
            });
        }
    });
}

//FUNCTION TO UPDATE CHILD COMMENT DYNAMICALLY
function updateChildComments(childCommentId) {
    let $contentToUpdate = document.getElementById(`${'child-comment-content' + childCommentId}`).textContent;
    let $input = $(`<input contenteditable="true" id="${'child-comment-input' + childCommentId}" type="text" autofocus class="form-control" required minlength="3" maxlength="255" value="${$contentToUpdate}">`);
    $(`#${'child-comment-content' + childCommentId}`).replaceWith($input);
    $(`#${'child-comment-input' + childCommentId}`).on('keypress',function(event) {
        if(event.key === "Enter") {
            const $URL = `http://localhost:8080/api/v1/comments/${childCommentId}`;
            const $VERB = 'PUT';
            const $DATATYPE = 'json';
            let postContent = document.getElementById(`${'child-comment-input' + childCommentId}`).value;

            let $data =
                {
                    "content": postContent,
                };

            $.ajax({
                type: $VERB,
                url: $URL,
                dataType: $DATATYPE,
                contentType: 'application/json; charset=utf-8',
                async: true,
                data: JSON.stringify($data),
                success: function (data) {
                },
                error: function () {
                    $input.replaceWith(`<p id="${'child-comment-content' + childCommentId}" style="font-size: small" class="text-muted">${postContent}</p>`);
                }
            });
        }
    });
}

//FUNCTION TO CREATE PARENT COMMENT
function createParentCommentOnEnterHit(userId, postId) {
    const $URL = `http://localhost:8080/api/v1/comments`;
    const $VERB = 'POST';
    const $DATATYPE = 'json';
    let postContent = document.getElementById(`${'parent-comment-create' + postId}`).value;

    let $data =
        {
            "content": postContent,
            "user": {
                "id": userId
            },
            "post": {
                "id": postId
            }
        };

    $.ajax({
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        contentType: 'application/json; charset=utf-8',
        async: true,
        data: JSON.stringify($data),
        success: function (data) {
        },
        error: function () {
            setTimeout(function () {
                window.location.reload();
            }, 200);
        }
    });
}

//FUNCTION TO CREATE CHILD COMMENT
function createChildCommentOnEnterHit(userId, postId, commentId) {
    const $URL = `http://localhost:8080/api/v1/comments`;
    const $VERB = 'POST';
    const $DATATYPE = 'json';
    let postContent = document.getElementById(`${'child-comment-create' + postId}`).value;

    let $data =
        {
            "content": postContent,
            "user": {
                "id": userId
            },
            "post": {
                "id": postId
            },
            "parentComment" : {
                "id": commentId
            }
        };

    $.ajax({
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        contentType: 'application/json; charset=utf-8',
        async: true,
        data: JSON.stringify($data),
        success: function (data) {
        },
        error: function () {
            setTimeout(function () {
                window.location.reload();
            }, 200);
        }
    });
}

//FUNCTION TO PAGINATE POSTS OF USER ON SCROLL ON TIMELINE-ABOUT PAGE
$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() >= $(document).height()) {
        $getAllPostsPageableByUserOnTimelineView();
    }
});

//FUNCTION TO PAGINATE POSTS OF USER ON CLICK "NEXT BUTTON" ON TIMELINE-ABOUT PAGE
$('#next-btn').on('click', function () {
    $getAllPostsPageableByUserOnTimelineView();
});

//FUNCTION TO CREATE POST LIKE WHEN CLICK "LIKE BUTTON"
$('#number_likes_post').on('click', function () {
    // let userId = $('#userId').attr('userId');
    let postId = $('#postId').attr('postId');
    const $URL = `http://localhost:8080/api/v1/likedPosts/posts/${postId}`;
    const $VERB = 'POST';
    const $DATATYPE = 'json';

    $.ajax({
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        async: true,
        success: function () {
            alert('Ok!');
        },
        error: function () {
            alert('Error create likedPost!');
        }
    });
});

//FUNCTION TO LIKE POSTS DYNAMICALLY
function createLikeToPost(postId) {
    const $URL = `http://localhost:8080/api/v1/likedPosts/posts/${postId}`;
    const $VERB = 'POST';
    const $DATATYPE = 'json';

    if (isLikeOfPostExist(postId)) {
    } else {
        $.ajax({
            type: $VERB,
            url: $URL,
            dataType: $DATATYPE,
            async: false,
            success: function () {
            },
            error: function () {
            }
        });
        getLikesOfPost(postId)
    }
}

//FUNCTION TO GET LIKES OF POSTS DYNAMICALLY
function getLikesOfPost(postId) {
    let $likeDiv = $(`#a-${postId}`);
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/likedPosts/posts/${postId}/count`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let likesCount = 0;

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (postLikes) {
            likesCount = postLikes;
        }
    });
    $likeDiv.replaceWith(`<a id="a-${postId}" class="btn text-red" style="font-size: medium" onclick="createLikeToPost(${postId})"><ion-icon name="heart" size="smaller"></ion-icon> ${likesCount}</a>`);
}

//FUNCTION TO LIKE COMMENT DYNAMICALLY
function createLikeToComment(commentId) {
    const $URL = `http://localhost:8080/api/v1/likedComments/${commentId}`;
    const $VERB = 'POST';
    const $DATATYPE = 'json';

    if (isLikeOfCommentExist(commentId)) {
    } else {
        $.ajax({
            type: $VERB,
            url: $URL,
            dataType: $DATATYPE,
            async: false,
            success: function () {
            },
            error: function () {
            }
        });
        getLikesOfComment(commentId);
    }
}

//FUNCTION TO GET LIKES OF COMMENT DYNAMICALLY
function getLikesOfComment(commentId) {
    let $likeDiv = $(`#${'comment-likes' + commentId}`);
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/likedComments/comment/${commentId}/count`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let commentLikesCount = 0;

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (commentLikes) {
            commentLikesCount = commentLikes;
        }
    });
    $likeDiv.replaceWith(`<a id="${'comment-likes' + commentId}" class="btn text-red" style="font-size: medium" onclick="createLikeToComment(${commentId})"><ion-icon name="heart" size="smaller"></ion-icon> ${commentLikesCount}</a>`);
}

//FUNCTION TO LIKE CHILD COMMENT DYNAMICALLY
function createLikeToChildComment(childCommentId) {
    const $URL = `http://localhost:8080/api/v1/likedComments/${childCommentId}`;
    const $VERB = 'POST';
    const $DATATYPE = 'json';

    if (isLikeOfCommentExist(childCommentId)) {
    } else {
        $.ajax({
            type: $VERB,
            url: $URL,
            dataType: $DATATYPE,
            async: false,
            success: function () {
            },
            error: function () {
            }
        });
        getLikesOfChildComment(childCommentId);
    }
}

//FUNCTION TO GET LIKES OF CHILD COMMENT DYNAMICALLY
function getLikesOfChildComment(childCommentId) {
    let $likeDiv = $(`#${'child-comment-likes' + childCommentId}`);
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/likedComments/comment/${childCommentId}/count`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let childCommentLikesCount = 0;

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (childCommentLikes) {
            childCommentLikesCount = childCommentLikes;
        }
    });
    $likeDiv.replaceWith(`<a id="${'child-comment-likes' + childCommentId}" class="btn text-red" style="font-size: medium" onclick="createLikeToComment(${childCommentId})"><ion-icon name="heart" size="smaller"></ion-icon> ${childCommentLikesCount}</a>`);
}

//FUNCTION TO DELETE A POST DYNAMICALLY
function deletePosts(postId) {
    let $postDiv = $(`#${'delete' + postId}`);
    $.ajax({
        async: true,
        type: 'DELETE',
        url: `http://localhost:8080/api/v1/posts/${postId}`,
        dataType: 'json',
        success: function () {
        },
        error: function () {
        }
    });
    $postDiv.empty();
}

//FUNCTION TO DELETE A COMMENT DYNAMICALLY
function deleteComments(commentId) {
    let $commentDiv = $(`#${'delete-comment' + commentId}`);
    $.ajax({
        async: true,
        type: 'DELETE',
        url: `http://localhost:8080/api/v1/comments/${commentId}`,
        dataType: 'json',
        success: function () {
        },
        error: function () {
        }
    });
    $commentDiv.empty();
}

//FUNCTION TO DELETE A COMMENT DYNAMICALLY
function deleteChildComments(childCommentId) {
    let $childCommentDiv = $(`#${'delete-child-comment' + childCommentId}`);
    $.ajax({
        async: true,
        type: 'DELETE',
        url: `http://localhost:8080/api/v1/comments/${childCommentId}`,
        dataType: 'json',
        success: function () {
        },
        error: function () {
        }
    });
    $childCommentDiv.empty();
}

//FUNCTION TO CHECK IF POST LIKE EXIST BY USER AND POST
function isLikeOfPostExist(postId) {
    let isExist = false;
    $.ajax({
        async: false,
        type: 'GET',
        url: `http://localhost:8080/api/v1/likedPosts/${postId}/exist`,
        dataType: 'json',
        success: function (result) {
            isExist = result;
        }
    });
    return isExist;
}

//FUNCTION TO CREATE POST LIKE ON BUTTON CLICK
function LikedPostCreate(postId) {
    const $URL = `http://localhost:8080/api/v1/likedPosts/posts/${postId}`;
    const $VERB = 'POST';
    const $DATATYPE = 'json';

    if (isLikeOfPostExist(postId)) {
    } else {
        $.ajax({
            type: $VERB,
            url: $URL,
            dataType: $DATATYPE,
            async: true,
            success: function () {
                setTimeout(function () {// wait for 5 secs(2)
                    location.reload(); // then reload the page.(3)
                }, 500);
                $('#a-' + postId).disable();
            },
            error: function () {
                setTimeout(function () {// wait for 5 secs(2)
                    location.reload(); // then reload the page.(3)
                }, 500);
                $('#a-' + postId).disable();
            }
        });
    }
}

//FUNCTION TO GET ALL POST LIKES OF POST BY POST ID
function getPostLikes(postId) {
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/likedPosts/posts/${postId}/count`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let likesCount = 0;

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (postLikes) {
            likesCount = postLikes;
        }
    });
    return likesCount;
}

//FUNCTION TO CHECK IF USERS ARE FRIENDS
function areUsersFriends(userId) {
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/friendShip/friends/user/${userId}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let areFriends = false;

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (result) {
            areFriends = result;
        }
    });
    return areFriends;
}

//FUNCTION TO CHECK IF POST LIKE EXIST BY USER AND POST
function isLikeOfCommentExist(commentId) {
    let isExist = false;
    $.ajax({
        async: false,
        type: 'GET',
        url: `http://localhost:8080/api/v1/likedComments/${commentId}/exist`,
        dataType: 'json',
        success: function (result) {
            isExist = result;
        }
    });
    return isExist;
}

//FUNCTION TO GET ALL COMMENT LIKES OF COMMENT BY COMMENT ID
function getCommentLikes(commentId) {
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/likedComments/comment/${commentId}/count`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let commentLikesCount = 0;

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (commentLikes) {
            commentLikesCount = commentLikes;
        }
    });
    return commentLikesCount;
}

//FUNCTION TO GET ALL PARENT COMMENTS OF POST
function getCommentData(postId) {
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/comments/parentComment/post/${postId}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let commentsResultArray = {};

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (comments) {
            commentsResultArray = comments;
        }
    });
    return commentsResultArray;
}

//FUNCTION TO CALL REST API AND GET ALL PARENT COMMENTS OF POST PAGEABLE
let countCommentPage = 0;
function getAllCommentsPageable(postId, countCommentPage) {
    const perPage = 2;
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/comments/parentComment/post/${postId}/pageable?page=${countCommentPage}&size=${perPage}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let parentCommentsResultArray = {};
    // countCommentPage++;

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (comments) {
            parentCommentsResultArray = comments;
        },
        error: function () {
            alert('Error loading parent comments!')
        }
    });
    return parentCommentsResultArray;
}

//FUNCTION TO GET ALL CHILD COMMENTS OF COMMENT
function getChildCommentData(commentId) {
    const $ASYNC = false;
    const $URL = `http://localhost:8080/api/v1/comments/childComments/comment/${commentId}`;
    const $VERB = 'GET';
    const $DATATYPE = 'json';
    let commentsResultArray = {};

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        success: function (comments) {
            commentsResultArray = comments;
        }
    });
    return commentsResultArray;
}

//FUNCTION TO UPDATE COMMENT
function updateCommentData(commentId) {
    const $ASYNC = true;
    const $URL = `http://localhost:8080/api/v1/comments/${commentId}`;
    const $VERB = 'PUT';
    const $DATATYPE = 'json';
    let comment_content = document.getElementById(`${commentId + 5000}`).value;
    // let comment_content = $('input[id=update-comment-content]').val();
    let $data =
        {
        "content": comment_content
        }

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify($data),
        success: function (data) {
            alert("OK")
        },
        error: function () {
            setTimeout(function () {
                location.reload();
            }, 500);
        }
    });
}

//FUNCTION TO UPDATE REPLY
function updateReplyData(replyId) {
    const $ASYNC = true;
    const $URL = `http://localhost:8080/api/v1/comments/${replyId}`;
    const $VERB = 'PUT';
    const $DATATYPE = 'json';
    let reply_content = $('input[id=update-reply-content]').val();
    let $data =
        {
            "content": reply_content
        }

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify($data),
        success: function (data) {
            alert("OK")
        },
        error: function () {
            setTimeout(function () {
                location.reload();
            }, 500);
        }
    });
}

//FUNCTION TO UPDATE POST
function updatePostData(postId) {
    const $ASYNC = true;
    const $URL = `http://localhost:8080/api/v1/comments/${postId}`;
    const $VERB = 'PUT';
    const $DATATYPE = 'json';
    let post_content = $('input[id=update-comment-content]').val();
    let $data =
        {
            "content": post_content
        }

    $.ajax({
        async: $ASYNC,
        type: $VERB,
        url: $URL,
        dataType: $DATATYPE,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify($data),
        success: function (data) {
            alert("OK")
        },
        error: function () {
            setTimeout(function () {
                location.reload();
            }, 500);
        }
    });
}

//FUNCTIONS FOR REQUEST FRIENDSHIP
function requestFriendship(userRequestId) {
    $.ajax({
        async: false,
        type: 'POST',
        url: `http://localhost:8080/api/v1/friendShip/${userRequestId}`,
        dataType: 'json',
        success: function () {
            window.location = location.href;
            // location.reload(true);
            // setTimeout(function () {// wait for 5 secs(2)
            //     window.location.reload(); // then reload the page.(3)
            // }, 1000);
        },
        error: function () {
            window.location = location.href;
            // setTimeout(function () {// wait for 5 secs(2)
            //     window.location.reload(); // then reload the page.(3)
            // }, 1000);
        }
    })
}

//FUNCTIONS FOR BLOCK FRIENDSHIP
function blockFriendship(userRequestId) {
    $.ajax({
        async: true,
        type: 'PUT',
        url: `http://localhost:8080/api/v1/friendShip/${userRequestId}/block`,
        dataType: 'json',
        success: function () {
            alert('Ok!');
        }
    })
}

//FUNCTIONS FOR UNBLOCK FRIENDSHIP
function unblockFriendship(userRequestId) {
    $.ajax({
        async: true,
        type: 'PUT',
        url: `http://localhost:8080/api/v1/friendShip/${userRequestId}/unblock`,
        dataType: 'json',
        success: function () {
            alert('Ok!');
        }
    })
}

//FUNCTIONS FOR REJECT FRIENDSHIP
function rejectFriendshipRequest(userRequestId) {
    $.ajax({
        async: true,
        type: 'PUT',
        url: `http://localhost:8080/api/v1/friendShip/${userRequestId}/3`,
        dataType: 'json',
        success: function () {
            alert('Ok!');
        }
    })
}

//FUNCTIONS FOR ACCEPT FRIENDSHIP
function acceptFriendshipRequest(userRequestId) {
    $.ajax({
        async: true,
        type: 'PUT',
        url: `http://localhost:8080/api/v1/friendShip/${userRequestId}/2`,
        dataType: 'json',
        success: function () {
            alert('Ok!');
        },
    })
}

//FUNCTIONS FOR DELETE USER
function deleteUser(userRequestId) {
    $.ajax({
        async: true,
        type: 'DELETE',
        url: `http://localhost:8080/api/v1/users/${userRequestId}`,
        dataType: 'json',
        success: function () {
            alert('Ok!');
        }
    })
}

$(document).ready(function () {
    $getAllUsersPageableOnIndexView();
    $getAllPostsPageableOnIndexView();
    $getAllPostsPageableByUserOnTimelineView();
});
