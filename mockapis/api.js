const express = require('express');
const app = express();
const cors = require('cors');
const multer = require('multer');
const storage = multer.memoryStorage();
const upload = multer({ storage });
app.use(cors(
    {
        origin: '*',
        methods: ['GET', 'POST', 'PUT', 'DELETE'],
        allowedHeaders: ['Content-Type', 'Authorization'],
    }
));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));


// Top Experts List API 
app.get('/api/top-experts', (req, res) => {
    const topExperts = [
        { id: 1, name: 'Naveen Verma', expertise: 'UI/UX Designer / Graphic Designer', languages: ['English', 'Hindi', 'Punjabi'], videoCallCharge: 488, audioCallCharge: 488, chatCharge: 488, rating: 4.5, topRated: true, insAvailable: true, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Online", twoMinFree: true, waitingTime: { hr: 0, min: 2, sec: 0 } },
        { id: 2, name: 'Ajay Goyal', expertise: 'Software Engineer', languages: ['English', 'Spanish'], videoCallCharge: 500, audioCallCharge: 500, chatCharge: 500, rating: 4.7, topRated: true, insAvailable: false, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Offline", twoMinFree: false },
        { id: 3, name: 'Amit Tandon', expertise: 'Data Scientist', languages: ['English', 'French'], videoCallCharge: 600, audioCallCharge: 600, chatCharge: 600, rating: 4.8, topRated: true, insAvailable: true, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Online", twoMinFree: true },
        { id: 4, name: 'Jai Kapoor', expertise: 'Project Manager', languages: ['English', 'German'], videoCallCharge: 700, audioCallCharge: 700, chatCharge: 700, rating: 4.6, topRated: true, insAvailable: false, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Busy", waitingTime: { hr: 1, min: 1, sec: 1 }, twoMinFree: false },
        { id: 5, name: 'Rahul Rana', expertise: 'DevOps Engineer', languages: ['English'], videoCallCharge: 800, audioCallCharge: 800, chatCharge: 800, rating: 4.9, topRated: true, insAvailable: true, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Online", twoMinFree: true },
        { id: 6, name: 'Dev Sharma', expertise: 'Cybersecurity Expert', languages: ['English', 'Italian'], videoCallCharge: 900, audioCallCharge: 900, chatCharge: 900, rating: 4.4, topRated: true, insAvailable: false, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Offline", twoMinFree: false },
        { id: 7, name: 'Astha Shah', expertise: 'Cloud Architect', languages: ['English', 'Japanese'], videoCallCharge: 1000, audioCallCharge: 1000, chatCharge: 1000, rating: 4.3, topRated: true, insAvailable: true, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Busy", waitingTime: { hr: 1, min: 1, sec: 1 }, twoMinFree: true },
        { id: 8, name: 'Shimali Qureshi', expertise: 'AI Researcher', languages: ['English', 'Chinese'], videoCallCharge: 1100, audioCallCharge: 1100, chatCharge: 1100, rating: 4.2, topRated: true, insAvailable: false, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Online", twoMinFree: false },
        { id: 9, name: 'Farrah Khan', expertise: 'Blockchain Developer', languages: ['English', 'Russian'], videoCallCharge: 1200, audioCallCharge: 1200, chatCharge: 1200, rating: 4.1, topRated: true, insAvailable: true, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Online", twoMinFree: true },
        { id: 10, name: 'Vijay Brar', expertise: 'Mobile App Developer', languages: ['English', 'Korean'], videoCallCharge: 1300, audioCallCharge: 1300, chatCharge: 1300, rating: 4.0, topRated: true, insAvailable: false, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Busy", waitingTime: { hr: 1, min: 1, sec: 1 }, twoMinFree: false },
        { id: 11, name: 'Aditya Kashyap', expertise: 'Web Developer', languages: ['English', 'Portuguese'], videoCallCharge: 1400, audioCallCharge: 1400, chatCharge: 1400, rating: 3.9, topRated: true, insAvailable: true, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Online", twoMinFree: true },
        { id: 12, name: 'Ankita Mishra', expertise: 'SEO Specialist', languages: ['English', 'Arabic'], videoCallCharge: 1500, audioCallCharge: 1500, chatCharge: 1500, rating: 3.8, topRated: true, insAvailable: false, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Offline", twoMinFree: false },
        { id: 13, name: 'Aman Gill', expertise: 'Content Writer', languages: ['English', 'Turkish'], videoCallCharge: 1600, audioCallCharge: 1600, chatCharge: 1600, rating: 3.7, topRated: true, insAvailable: true, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Busy", waitingTime: { hr: 1, min: 1, sec: 1 }, twoMinFree: true },
        { id: 14, name: 'Abrar Ul Haq', expertise: 'Digital Marketing Expert', languages: ['English', 'Hindi'], videoCallCharge: 1700, audioCallCharge: 1700, chatCharge: 1700, rating: 3.6, topRated: true, insAvailable: false, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Online", twoMinFree: false },
        { id: 15, name: 'Shakti Goyal', expertise: 'Graphic Designer', languages: ['English', 'French'], videoCallCharge: 1800, audioCallCharge: 1800, chatCharge: 1800, rating: 3.5, topRated: true, insAvailable: true, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Online", twoMinFree: true },
        { id: 16, name: 'Rajat Arora', expertise: 'Data Analyst', languages: ['English', 'Spanish'], videoCallCharge: 1900, audioCallCharge: 1900, chatCharge: 1900, rating: 3.4, topRated: true, insAvailable: false, imageURL: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D', status: "Offline", twoMinFree: false },
        { id: 17, name: 'Neha Sharma', expertise: 'Network Engineer', languages: ['English', 'German'], videoCallCharge: 2000, audioCallCharge: 2000, chatCharge: 2000, rating: 3.3, topRated: true, insAvailable: true, status: "Busy", imageURL: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D", waitingTime: { hr: 1, min: 1, sec: 1 }, twoMinFree: true },
        { id: 18, name: 'Bal Kishan', expertise: 'Database Administrator', languages: ['English', 'Italian'], videoCallCharge: 2100, audioCallCharge: 2100, chatCharge: 2100, rating: 3.2, topRated: true, insAvailable: false, imageURL: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D", status: "Offline", twoMinFree: false },
        { id: 19, name: 'Kunal Kapoor', expertise: 'System Analyst', languages: ['English', 'Chinese'], videoCallCharge: 2200, audioCallCharge: 2200, chatCharge: 2200, rating: 3.1, topRated: true, insAvailable: true, imageURL: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D", status: "Online", twoMinFree: true },
        { id: 20, name: 'Samay Khanna', expertise: 'IT Consultant', languages: ['English', 'Russian'], videoCallCharge: 2300, audioCallCharge: 2300, chatCharge: 2300, rating: 3.0, topRated: true, insAvailable: false, imageURL: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8dXNlcnxlbnwwfHwwfHx8MA%3D%3D", status: "Busy", waitingTime: { hr: 1, min: 1, sec: 1 }, twoMinFree: true },
    ];
    if (topExperts.length === 0) {
        return res.json({ status: false, message: "No Experts Found", data: [] });
    }
    res.json({ status: true, message: "Top Experts List", data: topExperts });
});

// Expert Professional Info API
app.post('/api/expert-professional-info', upload.single('file'), (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const expertProfessionalInfo = req.body; // Assuming the request body contains the expert's professional info
    expertProfessionalInfo.fileName = req.file ? req.file.originalname : null; // Adding file name to the response if a file was uploaded
    expertProfessionalInfo.filePath = req.file ? req.file.path : null; // Adding file path to the response if a file was uploaded
    if (!expertProfessionalInfo) {
        return res.json({ status: false, message: "Data hasn't been received", data: [] });
    }

    res.json({ status: true, message: "Expert Professional Info", data: expertProfessionalInfo });
})

// Create Post API 
app.post('/api/create-post', upload.array('files'), async (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    // if (!req.files || req.files.length === 0) {
    //     return res.json({ status: false, message: "No files uploaded", data: [] });
    // }

    // try {
    //     const filePromises = req.files.map((file) => {
    //         const newFile = new File({
    //             originalName: file.originalname,
    //             fileData: file.buffer,
    //             contentType: file.mimetype,
    //         });
    //         return newFile.save();
    //     });

    //     await Promise.all(filePromises);
    //     res.status(200).send('Files uploaded and saved to DB');
    // } catch (error) {
    //     console.error(error);
    //     res.status(500).send('Error saving files to DB');
    // }
    const files = req.files.map(file => {
        return {
            originalName: file.originalname,
            // fileData: file.buffer,
            contentType: file.mimetype,
        };
    }); // Assuming you want to process the files in some way
    const postData = req.body; // Assuming the request body contains the post data
    // postData.fileName = req.file ? req.file.originalname : null; // Adding file name to the response if a file was uploaded
    // postData.filePath = req.file ? req.file.path : null; // Adding file path to the response if a file was uploaded
    postData.files = files; // Adding files to the post data
    if (!postData) {
        return res.json({ status: false, message: "Data hasn't been received", data: [] });
    }

    res.json({ status: true, message: "Post Created Successfully", data: postData });
});

// Update Post API
app.post('/api/update-post', upload.array('files'), (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    // if (!req.files || req.files.length === 0) {
    //     return res.json({ status: false, message: "No files uploaded", data: [] });
    // }


    // const files = req.files.map(file => {
    //     return {
    //         originalName: file.originalname,
    //         // fileData: file.buffer,
    //         contentType: file.mimetype,
    //     };
    // }); // Assuming you want to process the files in some way


    const postData = req.body; // Assuming the request body contains the post data
    // postData.fileName = req.file ? req.file.originalname : req.body.fileName; // Adding file name to the response if a file was uploaded
    // postData.filePath = req.file ? req.file.path : req.body.fileName; // Adding file path to the response if a file was uploaded
    // postData.files = files; // Adding files to the post data
    if (!postData) {
        return res.json({ status: false, message: "Data hasn't been received", data: [] });
    }

    res.json({ status: true, message: "Post Edited Successfully", data: postData });
});

// Edit Post API
app.post('/api/edit-post/:postId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const postId = req.params.postId; // Assuming the request body contains the post ID to be deleted
    if (!postId) {
        return res.json({ status: false, message: "Post doesn't exist", data: [] });
    }
    const postData = {
        postId: postId,
        caption: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Molestias perspiciatis ut magni facilis culpa dolorum quaerat! Sint, fugiat reprehenderit similique at minima incidunt voluptatum qui cumque pariatur dolore culpa modi.",
        hashtags: ['#example', '#post'],
        location: 'Gurgaon, Haryana, India',
        files: [
            {
                originalName: "images.png",
                contentType: "image/png"
            },
            {
                originalName: "images.png",
                contentType: "image/png"
            }
        ]
    }

    res.json({ status: true, message: "Edit your post", data: postData });
});

// Delete Post API
app.post('/api/delete-post/:postId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const postId = req.params.postId; // Assuming the request body contains the post ID to be deleted
    if (!postId) {
        return res.json({ status: false, message: "Post doesn't exist", data: [] });
    }

    res.json({ status: true, message: "Post Deleted Successfully", data: { postId } });
});

// Get Posts for User API
app.get('/api/get-posts/:userId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const userId = req.params.userId; // Assuming the request body contains the user ID
    if (!userId) {
        return res.json({ status: false, message: "User doesn't exist", data: [] });
    }
    const posts = [
        {
            postId: 1,
            caption: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Molestias perspiciatis ut magni facilis culpa dolorum quaerat! Sint, fugiat reprehenderit similique at minima incidunt voluptatum qui cumque pariatur dolore culpa modi.",
            hashtags: ['#example', '#post'],
            location: 'Gurgaon, Haryana, India',
            files: [
                {
                    originalName: "images.png",
                    contentType: "image/png"
                },
                {
                    originalName: "images.png",
                    contentType: "image/png"
                }
            ]
        }
    ];
    if (posts.length === 0) {
        return res.json({ status: false, message: "No Posts Found", data: [] });
    }

    res.json({ status: true, message: "Here are all the posts", data: posts });
});

// Get Feed API
app.get('/api/get-feed/', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const feed = [
        {
            postId: 1,
            caption: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Molestias perspiciatis ut magni facilis culpa dolorum quaerat! Sint, fugiat reprehenderit similique at minima incidunt voluptatum qui cumque pariatur dolore culpa modi.",
            hashtags: ['#example', '#post'],
            location: 'Gurgaon, Haryana, India',
            files: [
                {
                    originalName: "images.png",
                    contentType: "image/png"
                },
                {
                    originalName: "images.png",
                    contentType: "image/png"
                }
            ]
        }
    ];
    if (feed.length === 0) {
        return res.json({ status: false, message: "No Post Found", data: [] });
    }

    res.json({ status: true, message: "Here is your feed", data: feed });
});

// Get Post Like Count API
app.get('/api/get-post-like-count/:postId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const postId = req.params.postId; // Assuming the request body contains the post ID
    if (!postId) {
        return res.json({ status: false, message: "Post doesn't exist", data: [] });
    }
    const likeCount = 100; // Simulating like count
    res.json({ status: true, message: "Post Like Count", data: { postId, likeCount } });
});

// Report Post API
app.post('/api/report-post/:postId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const postId = req.params.postId; // Assuming the request body contains the post ID
    if (!postId) {
        return res.json({ status: false, message: "Post doesn't exist", data: [] });
    }
    const reportReason = req.body.reason; // Assuming the request body contains the report reason
    if (!reportReason) {
        return res.json({ status: false, message: "Report reason not provided", data: [] });
    }

    res.json({ status: true, message: "Post Reported Successfully", data: { postId, reportReason } });
});

// Get Post Comments API
app.get('/api/get-post-comments/:postId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const postId = req.params.postId; // Assuming the request body contains the post ID
    if (!postId) {
        return res.json({ status: false, message: "Post doesn't exist", data: [] });
    }
    const comments = [
        { commentId: 1, text: "Great post!", userId: 1 },
        { commentId: 2, text: "Thanks for sharing!", userId: 2 },
    ];
    if (comments.length === 0) {
        return res.json({ status: false, message: "No Comments Found", data: [] });
    }

    res.json({ status: true, message: "Post Comments", data: comments });
});

// Get Nested Comments API
app.get('/api/get-nested-comments/:commentId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const commentId = req.params.commentId; // Assuming the request body contains the comment ID
    if (!commentId) {
        return res.json({ status: false, message: "Comment doesn't exist", data: [] });
    }
    const nestedComments = [
        { nestedCommentId: 1, text: "Nested comment 1", userId: 1 },
        { nestedCommentId: 2, text: "Nested comment 2", userId: 2 },
    ];
    if (nestedComments.length === 0) {
        return res.json({ status: false, message: "No Nested Comments Found", data: [] });
    }

    res.json({ status: true, message: "Nested Comments", data: nestedComments });
});

// Create Comment API
app.post('/api/create-comment/:postId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const postId = req.params.postId; // Assuming the request body contains the post ID
    if (!postId) {
        return res.json({ status: false, message: "Post doesn't exist", data: [] });
    }
    const commentData = req.body; // Assuming the request body contains the comment data
    if (!commentData) {
        return res.json({ status: false, message: "Comment data not provided", data: [] });
    }

    res.json({ status: true, message: "Comment Created Successfully", data: commentData });
});

// Create Nested Comment API
app.post('/api/create-nested-comment/:commentId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const commentId = req.params.commentId; // Assuming the request body contains the comment ID
    if (!commentId) {
        return res.json({ status: false, message: "Comment doesn't exist", data: [] });
    }
    const nestedCommentData = req.body; // Assuming the request body contains the nested comment data
    if (!nestedCommentData) {
        return res.json({ status: false, message: "Nested comment data not provided", data: [] });
    }

    res.json({ status: true, message: "Nested Comment Created Successfully", data: nestedCommentData });
});

// Delete Comment API
app.post('/api/delete-comment/:commentId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const commentId = req.params.commentId; // Assuming the request body contains the comment ID
    if (!commentId) {
        return res.json({ status: false, message: "Comment doesn't exist", data: [] });
    }

    res.json({ status: true, message: "Comment Deleted Successfully", data: { commentId } });
});

// Delete Nested Comment API
app.post('/api/delete-nested-comment/:nestedCommentId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const nestedCommentId = req.params.nestedCommentId; // Assuming the request body contains the nested comment ID
    if (!nestedCommentId) {
        return res.json({ status: false, message: "Nested Comment doesn't exist", data: [] });
    }

    res.json({ status: true, message: "Nested Comment Deleted Successfully", data: { nestedCommentId } });
});

// Edit Comment API
app.post('/api/edit-comment/:commentId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const commentId = req.params.commentId; // Assuming the request body contains the comment ID
    if (!commentId) {
        return res.json({ status: false, message: "Comment doesn't exist", data: [] });
    }
    const commentData = req.body; // Assuming the request body contains the comment data
    if (!commentData) {
        return res.json({ status: false, message: "Comment data not provided", data: [] });
    }

    res.json({ status: true, message: "Comment Edited Successfully", data: commentData });
});

// Edit Nested Comment API
app.post('/api/edit-nested-comment/:nestedCommentId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const nestedCommentId = req.params.nestedCommentId; // Assuming the request body contains the nested comment ID
    if (!nestedCommentId) {
        return res.json({ status: false, message: "Nested Comment doesn't exist", data: [] });
    }
    const nestedCommentData = req.body; // Assuming the request body contains the nested comment data
    if (!nestedCommentData) {
        return res.json({ status: false, message: "Nested comment data not provided", data: [] });
    }

    res.json({ status: true, message: "Nested Comment Edited Successfully", data: nestedCommentData });
});

// Report Comment API
app.post('/api/report-comment/:commentId', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const commentId = req.params.commentId; // Assuming the request body contains the comment ID
    if (!commentId) {
        return res.json({ status: false, message: "Comment doesn't exist", data: [] });
    }
    const reportReason = req.body.reason; // Assuming the request body contains the report reason
    if (!reportReason) {
        return res.json({ status: false, message: "Report reason not provided", data: [] });
    }

    res.json({ status: true, message: "Comment Reported Successfully", data: { commentId, reportReason } });
});

// Get list of bookings API
app.post('/api/bookings', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }

    const bookingStatus = req.query.status; // Assuming the request query contains the booking status filter
    if (!bookingStatus) {
        return res.json({ status: false, message: "Booking status not provided", data: [] });
    }

    let bookings = []
    if (bookingStatus === "completed") {
        bookings = [
            { bookingId: 1, expertId: 1, meetingInfo: { expertName: "Naveen Verma", designation: "Backend Developer", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 1, status: 'completed' },
            { bookingId: 2, expertId: 2, meetingInfo: { expertName: "Ajay Goyal", designation: "Software Engineer", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 2, status: 'completed' },
            { bookingId: 3, expertId: 3, meetingInfo: { expertName: "Amit Tandon", designation: "Data Scientist", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 3, status: 'completed' },
            { bookingId: 4, expertId: 4, meetingInfo: { expertName: "Jai Kapoor", designation: "Project Manager", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 4, status: 'completed' },
            { bookingId: 5, expertId: 5, meetingInfo: { expertName: "Rahul Rana", designation: "DevOps Engineer", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 5, status: 'completed' },
        ]
    }
    if (bookingStatus === "awaiting") {
        bookings = [
            { bookingId: 1, expertId: 1, meetingInfo: { expertName: "Naveen Verma", designation: "Backend Developer", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 1, status: 'awaiting' },
            { bookingId: 2, expertId: 2, meetingInfo: { expertName: "Ajay Goyal", designation: "Software Engineer", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 2, status: 'awaiting' },
            { bookingId: 3, expertId: 3, meetingInfo: { expertName: "Amit Tandon", designation: "Data Scientist", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 3, status: 'awaiting' },
            { bookingId: 4, expertId: 4, meetingInfo: { expertName: "Jai Kapoor", designation: "Project Manager", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 4, status: 'awaiting' },
            { bookingId: 5, expertId: 5, meetingInfo: { expertName: "Rahul Rana", designation: "DevOps Engineer", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 5, status: 'awaiting' },
        ];
    }
    if (bookingStatus === "upcoming") {
        bookings = [
            { bookingId: 1, expertId: 1, meetingInfo: { expertName: "Naveen Verma", designation: "Backend Developer", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 1, status: 'upcoming' },
            { bookingId: 2, expertId: 2, meetingInfo: { expertName: "Ajay Goyal", designation: "Software Engineer", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 2, status: 'upcoming' },
            { bookingId: 3, expertId: 3, meetingInfo: { expertName: "Amit Tandon", designation: "Data Scientist", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 3, status: 'upcoming' },
            { bookingId: 4, expertId: 4, meetingInfo: { expertName: "Jai Kapoor", designation: "Project Manager", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 4, status: 'upcoming' },
            { bookingId: 5, expertId: 5, meetingInfo: { expertName: "Rahul Rana", designation: "DevOps Engineer", dateAndTime: { date: 28, month: "feb", year: 2000, hr: 9, min: 50, sec: 0 } }, userId: 5, status: 'upcoming' },
        ];
    }
    if (bookings.length === 0) {
        return res.json({ status: false, message: "No Bookings Found", data: [] });
    }

    res.json({ status: true, message: "Bookings List", data: bookings });
});

// Call Settings

// Get list of availability API
app.get('/api/get-availability', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const availability = [
        {},
        {},
        {},
        {},
        {},
        {},
    ]
    if (availability.length === 0) {
        return res.json({ status: false, message: "No Availability Found", data: [] });
    }
    res.json({ status: true, message: "Availability List", data: availability });
});
// Set availability
app.post('/api/set-availability', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const availabilityData = req.body; // Assuming the request body contains the availability data
    if (!availabilityData) {
        return res.json({ status: false, message: "Availability data not provided", data: [] });
    }

    res.json({ status: true, message: "Availability Set Successfully", data: availabilityData });
});

// Vacation mode
app.post('/api/vacation-mode', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const vacationData = req.body; // Assuming the request body contains the vacation data
    if (!vacationData) {
        return res.json({ status: false, message: "Vacation data not provided", data: [] });
    }

    res.json({ status: true, message: "Vacation Mode Set Successfully", data: vacationData });
});

// Set pricing
app.post('/api/set-pricing', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const pricingData = req.body; // Assuming the request body contains the pricing data
    if (!pricingData) {
        return res.json({ status: false, message: "Pricing data not provided", data: [] });
    }

    res.json({ status: true, message: "Pricing Set Successfully", data: pricingData });
});

// Get services 
app.get('/api/get-services', (req, res) => {
    const isAuthenticated = true; // Simulating authentication check
    if (!isAuthenticated) {
        return res.json({ status: false, message: "Unauthorized", data: [] });
    }
    const services = [
        {
            id: 12345,
            name: 'Legal',
            level: 0,
            servicesL1: [
                {
                    id: 67890,
                    name: 'Family Law', parentServiceId: 12345, level: 1, servicesL2: [
                        { id: 21, name: 'Divorce and Separation', parentServiceId: 67890, level: 2, icon: 'https://static.vecteezy.com/system/resources/thumbnails/004/329/268/small/family-court-glyph-icon-silhouette-symbol-child-custody-family-law-proceedings-divorce-mediation-legal-separation-negative-space-isolated-illustration-vector.jpg' },
                        { id: 22, name: 'Marriage and Prenuptial Agreements', parentServiceId: 67890, level: 2, icon: 'https://static.vecteezy.com/system/resources/thumbnails/004/329/268/small/family-court-glyph-icon-silhouette-symbol-child-custody-family-law-proceedings-divorce-mediation-legal-separation-negative-space-isolated-illustration-vector.jpg' },
                        { id: 23, name: 'Child Custody and Support', parentServiceId: 67890, level: 2, icon: 'https://static.vecteezy.com/system/resources/thumbnails/004/329/268/small/family-court-glyph-icon-silhouette-symbol-child-custody-family-law-proceedings-divorce-mediation-legal-separation-negative-space-isolated-illustration-vector.jpg' },
                    ]
                },
                {
                    id: 67891,
                    name: 'Corporate and Business Law', parentServiceId: 12345, level: 1, servicesL2: [
                        { id: 24, name: 'Company Registration and Compliance', parentServiceId: 67891, level: 2, icon: 'https://static.vecteezy.com/system/resources/thumbnails/004/329/268/small/family-court-glyph-icon-silhouette-symbol-child-custody-family-law-proceedings-divorce-mediation-legal-separation-negative-space-isolated-illustration-vector.jpg' },
                        { id: 25, name: 'Intellectual Property and Trademark', parentServiceId: 67891, level: 2, icon: 'https://static.vecteezy.com/system/resources/thumbnails/004/329/268/small/family-court-glyph-icon-silhouette-symbol-child-custody-family-law-proceedings-divorce-mediation-legal-separation-negative-space-isolated-illustration-vector.jpg' },
                        { id: 26, name: 'Contracts and Agreements', level: 2, parentServiceId: 67891, level: 2, icon: 'https://static.vecteezy.com/system/resources/thumbnails/004/329/268/small/family-court-glyph-icon-silhouette-symbol-child-custody-family-law-proceedings-divorce-mediation-legal-separation-negative-space-isolated-illustration-vector.jpg' },
                    ]
                },

            ],
        },
        {
            id: 12346,
            name: 'Health',
            level: 0,
            servicesL1: [
                {
                    id: 67892, name: 'Nutrition and Dietetics', parentServiceId: 12346, level: 1, servicesL2: [
                        { id: 27, name: 'Personalized Meal Plans', parentServiceId: 12346, level: 2, icon: 'https://static.vecteezy.com/system/resources/previews/010/354/084/non_2x/sign-of-nutritionist-symbol-is-isolated-on-a-white-background-icon-color-editable-free-vector.jpg' },
                        { id: 28, name: 'Nutritional Counsel', parentServiceId: 12346, level: 2, icon: 'https://static.vecteezy.com/system/resources/previews/010/354/084/non_2x/sign-of-nutritionist-symbol-is-isolated-on-a-white-background-icon-color-editable-free-vector.jpg' },
                        { id: 29, name: 'Weight Management', parentServiceId: 12346, level: 2, icon: 'https://static.vecteezy.com/system/resources/previews/010/354/084/non_2x/sign-of-nutritionist-symbol-is-isolated-on-a-white-background-icon-color-editable-free-vector.jpg' },
                    ]
                },
                {
                    id: 67893,
                    name: 'Fitness and Exercise', parentServiceId: 12346, level: 1, servicesL2: [
                        { id: 30, name: 'Personal Training', parentServiceId: 12346, level: 2, icon: 'https://static.vecteezy.com/system/resources/previews/010/354/084/non_2x/sign-of-nutritionist-symbol-is-isolated-on-a-white-background-icon-color-editable-free-vector.jpg' },
                        { id: 31, name: 'Group Fitness Classes', parentServiceId: 12346, level: 2, icon: 'https://static.vecteezy.com/system/resources/previews/010/354/084/non_2x/sign-of-nutritionist-symbol-is-isolated-on-a-white-background-icon-color-editable-free-vector.jpg' },
                        { id: 32, name: 'Online Fitness Coaching', parentServiceId: 12346, level: 2, icon: 'https://static.vecteezy.com/system/resources/previews/010/354/084/non_2x/sign-of-nutritionist-symbol-is-isolated-on-a-white-background-icon-color-editable-free-vector.jpg' },
                    ]
                },
            ]
        }
    ];
    if (services.length === 0) {
        return res.json({ status: false, message: "No Services Found", data: [] });
    }
    res.json({ status: true, message: "Services List", data: services });
    res.json({ status: true, message: "Services List", data: services });
});



app.listen(3009, () => {
    console.log('Server is running on port 3009');
});

