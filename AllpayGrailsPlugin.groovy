class AllpayGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2 > *"

    // TODO Fill in these fields
    def title = "Grails Allpay Plugin" // Headline display name of the plugin
    def author = "George Mei"
    def authorEmail = "cofemei@gmail.com"
    def description = '''\
Allpay(歐付寶) online payment in Taiwan
'''

    def documentation = "https://github.com/cofemei/grails-allpay-plugin"
    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    def developers = [ [ name: "George Mei", email: "cofemei@gmail.com" ]]
    def issueManagement = [url: 'https://github.com/cofemei/grails-allpay-plugin/issues']
    def scm = [ url: "https://github.com/cofemei/grails-allpay-plugin/" ]
}
