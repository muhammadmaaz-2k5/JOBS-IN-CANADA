<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Privacy Policy &middot; Jobs in Canada</title>
    <meta name="description" content="How Jobs in Canada collects, uses, and protects your personal information.">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #1A6B3C;
            --primary-dark: #2D8A52;
            --bg: #FFFFFF;
            --bg-soft: #F4F7F5;
            --text: #1F2933;
            --muted: #5B6772;
            --border: #E2E8E4;
            --radius: 14px;
        }
        * { box-sizing: border-box; }
        html { scroll-behavior: smooth; }
        body {
            margin: 0;
            font-family: 'Plus Jakarta Sans', ui-sans-serif, system-ui, sans-serif;
            color: var(--text);
            background: var(--bg);
            line-height: 1.7;
            -webkit-font-smoothing: antialiased;
        }
        a { color: var(--primary); text-decoration: none; }
        a:hover { text-decoration: underline; }

        .site-header {
            border-bottom: 1px solid var(--border);
            background: var(--bg);
            position: sticky;
            top: 0;
            z-index: 10;
        }
        .site-header .inner {
            max-width: 880px;
            margin: 0 auto;
            padding: 18px 24px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 16px;
        }
        .brand {
            font-weight: 800;
            font-size: 1.15rem;
            color: var(--text);
        }
        .brand span { color: var(--primary); }
        .back-link {
            font-size: .9rem;
            font-weight: 600;
            color: var(--muted);
        }

        .hero {
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
            color: #fff;
            padding: 56px 24px 64px;
            text-align: center;
        }
        .hero h1 {
            margin: 0 0 12px;
            font-size: 2.2rem;
            font-weight: 800;
            letter-spacing: -0.02em;
        }
        .hero p {
            margin: 0;
            opacity: .9;
            font-size: 1.05rem;
        }

        .container {
            max-width: 880px;
            margin: 0 auto;
            padding: 40px 24px 64px;
        }

        .updated {
            display: inline-block;
            background: var(--bg-soft);
            border: 1px solid var(--border);
            color: var(--muted);
            font-size: .85rem;
            font-weight: 600;
            padding: 6px 14px;
            border-radius: 999px;
            margin-bottom: 28px;
        }

        nav.toc {
            background: var(--bg-soft);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            padding: 20px 24px;
            margin-bottom: 40px;
        }
        nav.toc h2 {
            margin: 0 0 12px;
            font-size: .95rem;
            text-transform: uppercase;
            letter-spacing: .08em;
            color: var(--muted);
        }
        nav.toc ol {
            margin: 0;
            padding-left: 22px;
            columns: 2;
            column-gap: 32px;
        }
        nav.toc li { margin: 6px 0; }

        section { margin-bottom: 40px; }
        section h2 {
            font-size: 1.4rem;
            font-weight: 700;
            margin: 0 0 14px;
            color: var(--text);
            scroll-margin-top: 90px;
        }
        section h3 {
            font-size: 1.05rem;
            font-weight: 700;
            margin: 22px 0 8px;
        }
        section p { margin: 0 0 14px; color: var(--text); }
        section ul { margin: 0 0 14px; padding-left: 22px; color: var(--text); }
        section li { margin: 6px 0; }

        .card {
            background: var(--bg-soft);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            padding: 20px 24px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 8px 0 0;
            font-size: .95rem;
        }
        th, td {
            text-align: left;
            padding: 12px 14px;
            border-bottom: 1px solid var(--border);
            vertical-align: top;
        }
        th { font-weight: 700; color: var(--muted); }

        .site-footer {
            border-top: 1px solid var(--border);
            background: var(--bg-soft);
            color: var(--muted);
            font-size: .9rem;
        }
        .site-footer .inner {
            max-width: 880px;
            margin: 0 auto;
            padding: 28px 24px;
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: center;
            justify-content: space-between;
        }

        @media (max-width: 640px) {
            .hero h1 { font-size: 1.7rem; }
            nav.toc ol { columns: 1; }
        }
    </style>
</head>
<body>
    <header class="site-header">
        <div class="inner">
            <div class="brand">Jobs<span>inCanada</span></div>
            <a class="back-link" href="{{ url('/') }}">&larr; Back to site</a>
        </div>
    </header>

    <div class="hero">
        <h1>Privacy Policy</h1>
        <p>Your trust matters. Here is how we handle your data.</p>
    </div>

    <main class="container">
        <span class="updated">Last updated: {{ date('F j, Y') }}</span>

        <nav class="toc" aria-label="Table of contents">
            <h2>Contents</h2>
            <ol>
                <li><a href="#overview">Overview</a></li>
                <li><a href="#information">Information We Collect</a></li>
                <li><a href="#use">How We Use Information</a></li>
                <li><a href="#sharing">Sharing Your Information</a></li>
                <li><a href="#storage">Data Storage &amp; Security</a></li>
                <li><a href="#cookies">Cookies &amp; Tracking</a></li>
                <li><a href="#rights">Your Rights</a></li>
                <li><a href="#children">Children's Privacy</a></li>
                <li><a href="#changes">Changes to This Policy</a></li>
                <li><a href="#contact">Contact Us</a></li>
            </ol>
        </nav>

        <section id="overview">
            <h2>1. Overview</h2>
            <p>
                Jobs in Canada ("we", "us", or "our") operates a mobile application and website that help
                job seekers discover employment opportunities across Canada. This Privacy Policy explains what
                personal information we collect, why we collect it, and the choices you have. By using our
                services, you agree to the practices described in this policy.
            </p>
        </section>

        <section id="information">
            <h2>2. Information We Collect</h2>
            <p>We collect information in the following categories:</p>
            <h3>a. Information you provide</h3>
            <ul>
                <li>Account details such as your name and email address when you register or contact us.</li>
                <li>Saved jobs, search preferences, and other content you choose to store in the app.</li>
                <li>Communications you send to our support team.</li>
            </ul>
            <h3>b. Information collected automatically</h3>
            <ul>
                <li>Device information (operating system, app version, device identifiers).</li>
                <li>Usage data such as pages viewed, jobs opened, and feature interactions.</li>
                <li>Approximate location when you use location-based job filters (with your permission).</li>
            </ul>
            <h3>c. Information from third parties</h3>
            <ul>
                <li>Job listings and employer details published by employers and administrators on our platform.</li>
            </ul>
        </section>

        <section id="use">
            <h2>3. How We Use Your Information</h2>
            <p>We use the information we collect to:</p>
            <ul>
                <li>Provide, maintain, and improve our job search services.</li>
                <li>Personalize job recommendations and search results.</li>
                <li>Send you notifications about new jobs or updates you request.</li>
                <li>Respond to your questions and provide customer support.</li>
                <li>Analyze usage to understand and enhance the user experience.</li>
                <li>Comply with legal obligations and enforce our terms.</li>
            </ul>
        </section>

        <section id="sharing">
            <h2>4. Sharing Your Information</h2>
            <p>
                We do not sell your personal information. We may share information with:
            </p>
            <ul>
                <li><strong>Service providers</strong> who help us operate the app (hosting, analytics, push notifications).</li>
                <li><strong>Employers</strong> only when you choose to apply for a job through an external apply link.</li>
                <li><strong>Legal authorities</strong> when required by law or to protect our rights and users.</li>
            </ul>
        </section>

        <section id="storage">
            <h2>5. Data Storage &amp; Security</h2>
            <p>
                Your data is stored on secure servers. We use industry-standard safeguards to protect
                information from unauthorized access, loss, or misuse. While we work hard to protect your
                data, no method of transmission or storage is completely secure.
            </p>
        </section>

        <section id="cookies">
            <h2>6. Cookies &amp; Tracking</h2>
            <p>
                Our website may use cookies and similar technologies to remember your preferences and measure
                traffic. You can control cookies through your browser settings. Our mobile app may use
                analytics SDKs to understand usage, which can be limited in your device settings.
            </p>
        </section>

        <section id="rights">
            <h2>7. Your Rights</h2>
            <p>Depending on your location, you may have the right to:</p>
            <div class="card">
                <table>
                    <thead>
                        <tr><th>Right</th><th>Description</th></tr>
                    </thead>
                    <tbody>
                        <tr><td>Access</td><td>Request a copy of the personal data we hold about you.</td></tr>
                        <tr><td>Correction</td><td>Ask us to fix inaccurate or incomplete information.</td></tr>
                        <tr><td>Deletion</td><td>Request that we delete your account and associated data.</td></tr>
                        <tr><td>Opt-out</td><td>Unsubscribe from marketing or notification messages.</td></tr>
                    </tbody>
                </table>
            </div>
            <p>
                To exercise these rights, contact us using the details in the
                <a href="#contact">Contact Us</a> section below.
            </p>
        </section>

        <section id="children">
            <h2>8. Children's Privacy</h2>
            <p>
                Our services are not directed to individuals under the age of 16, and we do not knowingly
                collect personal information from children. If you believe a child has provided us with
                personal data, please contact us and we will delete it.
            </p>
        </section>

        <section id="changes">
            <h2>9. Changes to This Policy</h2>
            <p>
                We may update this Privacy Policy from time to time. When we do, we will revise the
                "Last updated" date above and, where appropriate, notify you through the app or by email.
            </p>
        </section>

        <section id="contact">
            <h2>10. Contact Us</h2>
            <p>
                If you have any questions about this Privacy Policy or how we handle your data, please reach
                out to us:
            </p>
            <div class="card">
                <p style="margin:0;">
                    <strong>Jobs in Canada</strong><br>
                    Email: <a href="mailto:privacy@jobincanada.example">privacy@jobincanada.example</a><br>
                    Website: <a href="{{ url('/') }}">{{ url('/') }}</a>
                </p>
            </div>
        </section>
    </main>

    <footer class="site-footer">
        <div class="inner">
            <div>&copy; {{ date('Y') }} Jobs in Canada. All rights reserved.</div>
            <div>
                <a href="{{ route('privacy') }}">Privacy Policy</a>
            </div>
        </div>
    </footer>
</body>
</html>
