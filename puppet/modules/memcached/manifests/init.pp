class memcached {

  $memcached_conf = "/etc/memcached.conf"

  package { 'memcached':
    ensure => 'latest'
  }

  service { 'memcached':
    ensure => running,
    enable => true,
    require => Package['memcached'],
  }

}